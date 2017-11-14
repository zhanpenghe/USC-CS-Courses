import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Sentence {

    private List<AtomSentence> atomSentences;

    public Sentence(String str){
        this.atomSentences = new ArrayList<>();
        String[] atoms = str.split("\\|");
        for(int i = 0; i < atoms.length; i++){
            String atom = atoms[i].trim();
            atomSentences.add(new AtomSentence(atom));
        }
        normalize();
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
        normalize();
    }

    public Sentence(AtomSentence atom){
        this.atomSentences = new ArrayList<>();
        this.atomSentences.add(atom);
        normalize();
    }

    @Override
    public boolean equals(Object anotherSentence){
        Sentence sentence = (Sentence) anotherSentence;
        if(this.atomSentences.size()!=sentence.getAtomSentences().size()) return false;
        for(AtomSentence atom : atomSentences){

            if(!sentence.getAtomSentences().contains(atom)){
                System.out.println(atom);
                return false;
            }
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
                while(replacement!=null){
                    arguments.remove(arguments.size()-1);
                    arguments.add(new Term(replacement.get(1)));
                    replacement = AtomSentence.searchUnification(unification, replacement.get(1));
                }
            }
        }
        return new AtomSentence(new Predicate(sentence.getPredicate().getName(), sentence.getPredicate().getNumOfArgs(), arguments), sentence.getNegate());
    }

    public static List<Sentence> applyResolution(Sentence sentence1, Sentence sentence2){
        List<Sentence> result = new ArrayList<>();
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

    private void normalize(){
        int offset = 0;
        Map<String, Integer> symbols = new HashMap<>();

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
        }
    }
}
