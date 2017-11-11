import java.util.ArrayList;
import java.util.List;

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
}
