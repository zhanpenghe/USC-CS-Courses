import java.io.*;

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

    public static void main(String[] args) {

        String inputFilePath = "/Users/adamhzp/Desktop/workspace/csci561/hw1/out/resources/input1.txt";
        int boardSize = 0, numLizard, method;

        try{
            FileInputStream fstream = new FileInputStream(inputFilePath);
            BufferedReader buffer = new BufferedReader(new InputStreamReader(fstream));

            String line = buffer.readLine();
            if (line == null) {
                System.out.println("[ERROR] Failed to load the input file.");
                return;
            }
            method = line.equalsIgnoreCase("DFS")?1:(line.equalsIgnoreCase("BFS")?0:(line.equalsIgnoreCase("SA"))?2:-1);
            boardSize = Integer.parseInt(buffer.readLine());
            numLizard = Integer.parseInt(buffer.readLine());

            int i = 0;
            char[][] initialState = new char[boardSize][boardSize];
            while ((line = buffer.readLine()) != null && i < boardSize) {
                for (int j = 0; j < boardSize; j++) {
                    initialState[i][j] = line.charAt(j);
                }
                i++;
            }
            if(Search.cornerCaseCheck(initialState, numLizard)) {
                Search search = new Search(new Node(initialState, 0), boardSize, numLizard, method);
                //search.printSearch();
                Node result = search.start();
                if (result != null) {
                    System.out.println("\nSUCCESS!");
                    System.out.println(result);
                    System.out.println("Strong goal check: " + Node.strongGoalCheck(result, numLizard));
                    writeToOutput("OK\n" + result.toString());
                }
                else {
                    System.out.println("\nFAIL!");
                    writeToOutput("FAIL");
                }
            }else{
                System.out.println("CORNER CASE.\nFAIL!");
                writeToOutput("FAIL");
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
