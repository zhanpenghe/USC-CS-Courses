import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static void writeToOutput(String s)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("./output.txt", false));
            writer.append(s);
            writer.close();
        }catch(Exception e)
        {
            System.out.println("FAILED TO WRITE OUTPUT FILE.");
        }
    }

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
            //Sentence sentence1 = new Sentence("~Parent(Liz, x0) | ~Ancestor(x0, Billy)");
            //Sentence sentence2 = new Sentence(" ~Parent(x0, x1) | ~Ancestor(x1, x2) | Ancestor(x0, x2)");
            //System.out.println(Sentence.applyResolution(sentence1, sentence2));

            //System.out.println(KB.query3(queries.get(0)));

            boolean[] results = new boolean[queries.size()];
            for(int i = 0; i<results.length; i++){
                //System.out.println("============Query "+i+"============");
                results[i] = KB.query3(queries.get(i));
            }

            StringBuilder result = new StringBuilder();
            for(int i = 0; i<results.length; i++){
                result.append(results[i]?"TRUE":"FALSE");
                if(i<results.length-1) result.append("\n");
            }

            writeToOutput(result.toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
