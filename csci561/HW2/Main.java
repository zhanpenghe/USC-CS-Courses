import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {
        String inputFilePath = "input.txt";

        try{
            FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fstream));
            int size = Integer.parseInt(buffer.readLine());
            int numOfTypes = Integer.parseInt(buffer.readLine());
            float time = Float.parseFloat(buffer.readLine());
            char[][] initial = new char[size][size];
            String line;
            for(int i = 0; i<size; i++)
            {
                line = buffer.readLine();
                for(int j = 0; j<size; j++) {
                    initial[i][j] = line.charAt(j);
                }
            }

            Minimax mm = new Minimax(initial, size, numOfTypes, time);
            mm.testExpand();


            /*Node test = new Node(1,2,initial);
            test.gravity();
            System.out.println(test);*/
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
