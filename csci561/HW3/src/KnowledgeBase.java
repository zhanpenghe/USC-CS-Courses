import java.util.ArrayList;
import java.util.List;

public class KnowledgeBase {

    private List<Sentence> sentences;

    public KnowledgeBase(){
        this.sentences = new ArrayList<>();
    }

    public void addSentence(Sentence sentence){
        this.sentences.add(sentence);
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("KB:\n");
        for(Sentence sentence: sentences){
            sb.append(sentence);
            sb.append("\n");
        }
        return sb.toString();
    }

}
