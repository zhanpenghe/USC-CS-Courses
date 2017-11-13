import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args){
        String inputFilePath = "input.txt";

        int numOfQuery, numOfKnowledge = 0;
        List<Sentence> queries = new ArrayList<>();
        KnowledgeBase KB = new KnowledgeBase();

        try{
            FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fstream));

            numOfQuery = Integer.parseInt(buffer.readLine());
            for(int i = 0; i<numOfQuery; i++) queries.add(new Sentence(buffer.readLine().trim()));

            numOfKnowledge = Integer.parseInt(buffer.readLine());
            for(int i = 0; i<numOfKnowledge; i++) KB.addSentence(new Sentence(buffer.readLine().trim()));

            System.out.println(KB);

            //testing for unification
            AtomSentence atom1 = new AtomSentence("~A(Adam,Adam,k)");
            AtomSentence atom2 = new AtomSentence("A(c, k, Adam2)");

            List<List<Term>> unification = new ArrayList<>();

            AtomSentence.unify(atom1, atom2, unification);
            /*for(List<Term> terms: unification){
                System.out.print(terms.get(0)+"/"+terms.get(1)+",");
            }
            */
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
