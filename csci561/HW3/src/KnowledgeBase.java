import java.util.*;

public class KnowledgeBase {

    private List<Sentence> sentences;

    public KnowledgeBase(){
        this.sentences = new ArrayList<>();
    }

    public void addSentence(Sentence sentence){
        this.sentences.add(sentence);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KB:\n");
        for(Sentence sentence: sentences){
            sb.append(sentence);
            sb.append("\n");
        }
        return sb.toString();
    }

    private void printSentences(List<Sentence> clauses){
        System.out.println("------\nKB SIZE: "+clauses.size());
        Integer count = 0;
        for(Sentence sentence: clauses){
            System.out.println(count+"\t"+sentence.toString());
            count++;
        }
    }

    public Boolean query3(AtomSentence query){

        List<Sentence> clauses = new ArrayList<>();
        for(Sentence sentence : this.sentences) clauses.add(sentence.clone());

        Sentence negated = new Sentence(new AtomSentence(query.getPredicate(), !query.getNegate()));
        clauses.add(negated);
        int offset = clauses.size()-1;
        while(true){
            printSentences(clauses);
            List<Sentence> news = new ArrayList<>();
            for(int i = 0; i<clauses.size(); i++){

                for(int j = Math.max(i+1, offset); j<clauses.size(); j++){
                    //System.out.print("\rComparing sentences "+i+" and "+j+".");
                    Sentence c1 = clauses.get(i);
                    Sentence c2 = clauses.get(j);
                    List<Sentence> resolvents = Sentence.applyResolution(c1, c2);
                    if(resolvents==null) return true;
                    //System.out.println("Result:");
                    //for(Sentence s: resolvents) System.out.println(s);
                    for(Sentence sentence: resolvents){
                        if(!news.contains(sentence)) news.add(sentence);
                    }
                }
            }
            offset = clauses.size();
            boolean isFalse = true;
            for(Sentence sentence: news){
                if(!clauses.contains(sentence)){
                    isFalse = false;
                    clauses.add(sentence);
                }
            }
            //System.out.println(offset+" "+clauses.size());
            if(isFalse || clauses.size()>10000) return false;
        }
    }

}
