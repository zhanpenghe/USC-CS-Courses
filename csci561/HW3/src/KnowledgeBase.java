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

    public Boolean query(AtomSentence query){
        List<Sentence> queue = new ArrayList<>();
        List<Sentence> records = new ArrayList<>();
        Sentence negated = new Sentence(new AtomSentence(query.getPredicate(), !query.getNegate()));

        queue.add(negated);

        while(!queue.isEmpty()){
            Sentence sentence1= queue.remove(0);

            for(Sentence sentence2: this.sentences) {
                System.out.println("-------\nResolution:\nSentence1: "+sentence1+"\nSentence2: "+sentence2);
                List<Sentence> resolutionResult = Sentence.applyResolution(sentence1, sentence2);
                System.out.println("Result: "+resolutionResult+"\n");
                if (resolutionResult == null) return true;
                for (Sentence result : resolutionResult) {
                    if (!records.contains(result)) {
                        queue.add(0, result);
                        records.add(result);
                    }

                }
            }
        }
        return false;
    }

}
