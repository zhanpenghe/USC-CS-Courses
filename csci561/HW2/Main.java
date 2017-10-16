import java.io.*;

public class Main {

    private static void writeToOutput(String s, String filePath) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false));
            writer.append(s);
            writer.close();
        }catch(Exception e)
        {
            System.out.println("[ERROR] Failed to write output to the file("+filePath+").\n");
        }
    }

    public static void main(String[] args) {

        String inputFilePath = "input.txt";

        try{
            long startTime = System.currentTimeMillis();

            FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fstream));

            int size = Integer.parseInt(buffer.readLine().trim());
            int numOfTypes = Integer.parseInt(buffer.readLine().trim());
            float time = Float.parseFloat(buffer.readLine().trim());

            char[][] initial = new char[size][size];
            String line;
            for(int i = 0; i<size; i++)
            {
                line = buffer.readLine();
                for(int j = 0; j<size; j++) {
                    //System.out.println(i+"..."+j);
                    initial[i][j] = line.charAt(j);
                }
            }

            Minimax mm = new Minimax(initial, size, numOfTypes, time);
            mm.setStartTime(startTime);
            String result = mm.run();
            writeToOutput(result, "output.txt");
            long endTime = System.currentTimeMillis();

            System.out.println("StartTime:\t"+startTime+"\nEndTime:\t"+endTime+"\nUsed time:\t"+(endTime-startTime)/1000.0+" s");
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
