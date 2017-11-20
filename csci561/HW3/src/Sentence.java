import java.util.*;

public class Sentence {

    private List<AtomSentence> atomSentences;

    public Sentence(String str){
        this.atomSentences = new ArrayList<>();
        String[] atoms = str.split("\\|");
        for(int i = 0; i < atoms.length; i++){
            String atom = atoms[i].trim();
            atomSentences.add(new AtomSentence(atom));
        }
        sort();
        standardize();
    }

    public Sentence(Sentence sentence1, Sentence sentence2, AtomSentence a1, AtomSentence a2, List<List<Term>> unification){
        this.atomSentences = new ArrayList<>();
        for(AtomSentence atom: sentence1.getAtomSentences()){
            if(atom.equals(a1)) continue;
            AtomSentence temp = Sentence.applyUnification(unification, atom);
            if(temp != null) this.atomSentences.add(temp);
        }
        for(AtomSentence atom: sentence2.getAtomSentences()){
            if(atom.equals(a2)) continue;
            AtomSentence temp = Sentence.applyUnification(unification, atom);
            if(temp != null) this.atomSentences.add(temp);
        }
        //System.out.println(unification);
        factoring();
        sort();
        standardize();
    }

    public Sentence(AtomSentence atom){
        this.atomSentences = new ArrayList<>();
        this.atomSentences.add(atom);
        sort();
        standardize();
    }

    public Sentence(List<AtomSentence> atoms){
        this.atomSentences = atoms;
    }

    @Override
    public boolean equals(Object anotherSentence){
        Sentence sentence = (Sentence) anotherSentence;
        if(this.atomSentences.size()!=sentence.getAtomSentences().size()) return false;
        for(AtomSentence atom : atomSentences){
            if(!sentence.getAtomSentences().contains(atom)) return false;
        }
        return true;
    }

    public List<AtomSentence> getAtomSentences() {
        return atomSentences;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < atomSentences.size(); i++){
            sb.append(atomSentences.get(i).toString());
            if(i<atomSentences.size()-1) sb.append(" | ");
        }

        return sb.toString();
    }

    private static AtomSentence applyUnification(List<List<Term>> unification, AtomSentence sentence){
        List<Term> arguments =  new ArrayList<>();
        for(int i = 0; i<sentence.getPredicate().getNumOfArgs(); i++){
            List<Term> replacement = AtomSentence.searchUnification(unification, sentence.getPredicate().getArgument(i));
            if(replacement==null) arguments.add(new Term(sentence.getPredicate().getArgument(i)));
            else{
                arguments.add(new Term(replacement.get(1)));
                replacement = AtomSentence.searchUnification(unification, replacement.get(1));
                while(replacement!=null){
                    arguments.remove(arguments.size()-1);
                    arguments.add(new Term(replacement.get(1)));
                    replacement = AtomSentence.searchUnification(unification, replacement.get(1));
                }
            }
        }
        return new AtomSentence(new Predicate(sentence.getPredicate().getName(), sentence.getPredicate().getNumOfArgs(), arguments), sentence.getNegate());
    }

    public static List<Sentence> applyResolution(Sentence c1, Sentence c2){
        List<Sentence> result = new ArrayList<>();
        Sentence sentence1 = c1.clone();
        Sentence sentence2 = c2.clone();
        //pre-process variables
        int offset = 0;
        Map<String, Integer> mapping = new HashMap<>();
        for(AtomSentence a1: sentence1.getAtomSentences()){
            for(Term arg: a1.getPredicate().getArguments()){
                if(arg.getType()==Term.VARIABLE){
                    if(!mapping.containsKey(arg.getValue())){
                        mapping.put(arg.getValue(), offset);
                        offset++;
                    }
                    arg.setValue("x"+mapping.get(arg.getValue()));
                }
            }
        }
        mapping.clear();
        for(AtomSentence a2: sentence2.getAtomSentences()){
            for(Term arg: a2.getPredicate().getArguments()){
                if(arg.getType()==Term.VARIABLE){
                    if(!mapping.containsKey(arg.getValue())){
                        mapping.put(arg.getValue(), offset);
                        offset++;
                    }
                    arg.setValue("x"+mapping.get(arg.getValue()));
                }
            }
        }
        /*System.out.println("-----\nResolution:");
        System.out.println(sentence1);
        System.out.println(sentence2);*/

        for(AtomSentence a1: sentence1.getAtomSentences()){
            for(AtomSentence a2: sentence2.getAtomSentences()){
                List<List<Term>> unification = new ArrayList<>();
                if(a1.getNegate()==!a2.getNegate() && AtomSentence.unify(a1,a2, unification)){
                    Sentence sentence = new Sentence(sentence1, sentence2, a1, a2, unification);
                    if(sentence.getAtomSentences().size()==0) return null;  //reach contradiction
                    result.add(sentence);
                }
            }
        }
        return result;
    }

    private void factoring(){
        List<AtomSentence> temp = new ArrayList<>();
        List<List<Term>> unification = new ArrayList<>();
        for(AtomSentence atom: this.atomSentences){
            boolean ok = true;
            for(AtomSentence re: temp) {
                if(atom.getNegate()==re.getNegate() && AtomSentence.unify(atom, re, unification)){
                    ok = false;
                    //System.out.println(unification);
                    break;
                }
            }
            if(ok) temp.add(atom);

        }
        List<AtomSentence> result = new ArrayList<>();
        for(AtomSentence sentence:temp) result.add(Sentence.applyUnification(unification, sentence));
        this.atomSentences = result;
        //System.out.println(result);
    }

    private void standardize(){
        int offset = 0;
        Map<String, Integer> symbols = new HashMap<>();
        List<AtomSentence> result = new ArrayList<>();
        for(AtomSentence atom: this.atomSentences){
            for(Term term: atom.getPredicate().getArguments()){
                if(term.getType()==Term.VARIABLE){
                    if(!symbols.containsKey(term.getValue())){
                        symbols.put(term.getValue(), offset);
                        offset+=1;
                    }
                    term.setValue("x"+symbols.get(term.getValue()));
                }
            }
            if(!result.contains(atom)) result.add(atom);
        }
        this.atomSentences = result;
    }

    private void sort(){
        Collections.sort(this.atomSentences, new Comparator<AtomSentence>() {
            @Override
            public int compare(AtomSentence o1, AtomSentence o2) {
                if(o1.getNegate()==true && o2.getNegate()==false) return 1;
                if(o1.getNegate()==false && o2.getNegate()==true) return -1;
                return o1.getPredicate().getName().compareTo(o2.getPredicate().getName());
            }
        });
    }

    public Sentence clone(){
        List<AtomSentence> atoms = new ArrayList<>();
        for(AtomSentence atom: this.atomSentences){
            atoms.add(atom.clone());
        }
        return new Sentence(atoms);
    }
}
