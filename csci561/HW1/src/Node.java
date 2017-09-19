import java.util.Arrays;

public class Node {

    private char[][] state;
    private int numOfLizard;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node node = (Node) o;

        return Arrays.deepEquals(state, node.state);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(state);
    }

    public char[][] getState() {
        return state;
    }

    public int getNumOfLizard() {
        return numOfLizard;
    }

    public Node(char state[][], int numOfLizard)
    {
        this.state = state;
        this.numOfLizard = numOfLizard;
    }

    public boolean goalCheck(int n)
    {
        return (this.numOfLizard == n);
    }

    public void printNode()
    {
        System.out.println("----"+this.numOfLizard+"---");
        for(int i=0; i<this.state.length; i++)
        {
            for(int j=0; j<this.state[i].length;j++)
            {
                System.out.print(state[i][j]+"\t");
            }
            System.out.println();
        }
    }

    public static Node makeNode(char[][] prevState, int prevNumOfLizard, int i, int j, boolean zero)
    {
        char[][] currState = new char[prevState.length][prevState.length];
        int m = 0, n;
        for(; m<currState.length; m++)
        {
            for (n=0; n<currState.length; n++){
                currState[m][n] = prevState[m][n];
            }
        }

        if(zero)
        {
            for(m = i, n = j; n<prevState.length; n++)
            {
                if(currState[m][n] == '2') break;
                currState[m][n] = '3';
            }
            return new Node(currState, prevNumOfLizard);
        }

        currState[i][j] = '1';

        //Update the map
        for(m=i-1;m>=0;m--){
            if(currState[m][j]=='2') break;
            currState[m][j] = '3';
        }

        for(m=j-1;m>=0;m--){
            if(currState[i][m]=='2') break;
            currState[i][m] = '3';
        }

        for(m=i+1;m<currState.length;m++){
            if(currState[m][j]=='2') break;
            currState[m][j] = '3';
        }

        for(m=j+1;m<currState.length;m++){
            if(currState[i][m]=='2') break;
            currState[i][m] = '3';
        }

        for(m=i-1, n=j-1; m>=0 && n>=0; m--, n--)
        {
            if(currState[m][n]=='2') break;
            currState[m][n] = '3';
        }

        for(m=i+1, n=j-1; m<currState.length && n>=0; m++, n--)
        {
            if(currState[m][n]=='2') break;
            currState[m][n] = '3';
        }

        for(m=i+1, n=j+1; m<currState.length && n<currState.length; m++, n++)
        {
            if(currState[m][n]=='2') break;
            currState[m][n] = '3';
        }


        for(m=i-1, n=j+1; m>=0 && n<currState.length; m--, n++)
        {
            if(currState[m][n]=='2') break;
            currState[m][n] = '3';
        }

        return new Node(currState, prevNumOfLizard+1);
    }

    public String toString()
    {
        String result = "";
        for(int i=0; i<this.state.length; i++)
        {
            for(int j=0; j<this.state[i].length;j++)
            {
                result+=((state[i][j]=='3'?'0':state[i][j]));
            }
            result+="\n";
        }
        return result;
    }

    public static boolean strongGoalCheck(Node node, int num)
    {
        int lizardCount = 0, m, n;
        char[][] currState = node.state;
        for(int i = 0; i < currState.length; i++) {
            for (int j = 0; j < currState.length; j++) {
                if (currState[i][j] != '1') continue;

                lizardCount += 1;
                //check all dimensions
                for (m = i - 1; m >= 0; m--) {
                    if (currState[m][j] == '2') break;
                    if (currState[m][j] == '1') return false;
                }

                for (m = j - 1; m >= 0; m--) {
                    if (currState[i][m] == '2') break;
                    if (currState[i][m] == '1') return false;
                }

                for (m = i + 1; m < currState.length; m++) {
                    if (currState[m][j] == '2') break;
                    if (currState[m][j] == '1') return false;
                }

                for (m = j + 1; m < currState.length; m++) {
                    if (currState[i][m] == '2') break;
                    if (currState[i][m] == '1') return false;
                }

                for (m = i - 1, n = j - 1; m >= 0 && n >= 0; m--, n--) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') return false;
                }

                for (m = i + 1, n = j - 1; m < currState.length && n >= 0; m++, n--) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') return false;
                }

                for (m = i + 1, n = j + 1; m < currState.length && n < currState.length; m++, n++) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') return false;
                }

                for (m = i - 1, n = j + 1; m >= 0 && n < currState.length; m--, n++) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') return false;
                }
            }
        }
        return (lizardCount == num);
    }

    public void setState(char[][] state) {
        this.state = state;
    }

    public boolean random(int n)
    {
        int i = 0, randomI, randomJ;
        while(i<n)
        {
            randomI = (int)(Math.random()*(state.length));
            randomJ = (int)(Math.random()*(state.length));
            if(this.state[randomI][randomJ]=='0') {
                i += 1;
                this.state[randomI][randomJ] = '1';
            }
        }
        return true;
    }

}