import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){
        String inputFilePath = "input.txt";

        int numOfQuery, numOfKnowledge = 0;
        List<AtomSentence> queries = new ArrayList<>();
        KnowledgeBase KB = new KnowledgeBase();

        try{
            FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fstream));

            numOfQuery = Integer.parseInt(buffer.readLine());
            for(int i = 0; i<numOfQuery; i++) queries.add(new AtomSentence(buffer.readLine().trim()));

            numOfKnowledge = Integer.parseInt(buffer.readLine());
            for(int i = 0; i<numOfKnowledge; i++) KB.addSentence(new Sentence(buffer.readLine().trim()));

            System.out.println(KB);

            //testing for resolution
            /*Sentence sentence1 = new Sentence("A(c, a, k) | B(a, k)");
            Sentence sentence2 = new Sentence("~A(a,c,Mandy)|C(b, Adam)");
            System.out.println(Sentence.applyResolution(sentence1, sentence2));
            */
            //System.out.println(KB.query(queries.get(0)));

            boolean[] results = new boolean[queries.size()];
            for(int i = 0; i<results.length; i++) results[i] = KB.query(queries.get(i));
            for(int i = 0; i<results.length; i++) System.out.println(results[i]);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
