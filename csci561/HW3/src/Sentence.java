import java.util.ArrayList;
import java.util.List;
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
    }

    public Sentence(Sentence anotherSentence, AtomSentence toBeRemoved, List<List<Term>> unification){
        this.atomSentences = new ArrayList<>();
        for(AtomSentence atom: anotherSentence.getAtomSentences()){
            if(atom.equals(toBeRemoved)) continue;
            AtomSentence temp = Sentence.applyUnification(unification, atom);
            if(temp != null) this.atomSentences.add(temp);
        }
    }

    public Boolean equals(Sentence sentence){
        if(this.atomSentences.size()!=sentence.getAtomSentences().size()) return false;

        for(AtomSentence atom : this.getAtomSentences()){
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
        //todo
        return null;
    }

    public static List<Sentence> applyResolution(AtomSentence atom, Sentence sentence){

        List<Sentence> result = new ArrayList<>();

        for(AtomSentence a: sentence.getAtomSentences()){
            List<List<Term>> unification = new ArrayList<>();
            if(a.getNegate()==!atom.getNegate() && AtomSentence.unify(atom,a, unification)){
                result.add(new Sentence(sentence, a, unification));
            }
        }

        return result;
    }
}
