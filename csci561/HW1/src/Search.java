import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Search {

    List<Node> nodes = null;
    Node saState = null;
    Random rand;


    int method = -1;//0==BFS, 1==DFS

    int boardSize;
    int numLizard;

    public Search(Node node, int boardSize, int numLizard, int method)
    {
        this.boardSize = boardSize;
        this.numLizard = numLizard;
        this.method = method;
        if(this.method == 1||this.method ==0){
            nodes = new LinkedList<>();
            nodes.add(node);
        }else if (this.method == 2)
        {
            saState = node;
            rand = new Random();
        }
    }
    private double schedule (double n) {
        return Math.min((double)this.boardSize, (double)this.numLizard)/Math.log(n)/(Math.min(3.5, this.boardSize));
    }

    public static boolean cornerCaseCheck(char[][] state, int n)
    {
        int empty = 0;
        int tree=0;
        for(int i = 0; i<state.length; i++)
        {
            for (int j = 0; j<state.length; j++)
            {
                if(state[i][j] == '0') empty+=1;
                if(state[i][j] == '2') tree+=1;
            }
        }
        if(empty<n) return false;
        if(tree==0 && n>state.length) return false;
        return true;
    }

    private Node removeHead() {
        try {
            return nodes.remove(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Node start()
    {
        if(this.method == 1||this.method ==0) return startDFSOrBFS();
        else if(this.method == 2) return startSA();
        else return null;
    }

    private int value(char[][] currState)
    {
        int result = 0;
        int m,n;

        for(int i = 0; i < currState.length; i++) {
            for (int j = 0; j < currState.length; j++) {
                if (currState[i][j] != '1') continue;

                //check all dimensions
                for (m = i - 1; m >= 0; m--) {
                    if (currState[m][j] == '2') break;
                    if (currState[m][j] == '1') result+=1;
                }

                for (m = j - 1; m >= 0; m--) {
                    if (currState[i][m] == '2') break;
                    if (currState[i][m] == '1') result+=1;
                }

                for (m = i + 1; m < currState.length; m++) {
                    if (currState[m][j] == '2') break;
                    if (currState[m][j] == '1') result+=1;
                }

                for (m = j + 1; m < currState.length; m++) {
                    if (currState[i][m] == '2') break;
                    if (currState[i][m] == '1') result+=1;
                }

                for (m = i - 1, n = j - 1; m >= 0 && n >= 0; m--, n--) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') result+=1;
                }

                for (m = i + 1, n = j - 1; m < currState.length && n >= 0; m++, n--) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') result+=1;
                }

                for (m = i + 1, n = j + 1; m < currState.length && n < currState.length; m++, n++) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') result+=1;
                }

                for (m = i - 1, n = j + 1; m >= 0 && n < currState.length; m--, n++) {
                    if (currState[m][n] == '2') break;
                    if (currState[m][n] == '1') result+=1;
                }
            }
        }

        return -1*result; // always negative.. the better the larger
    }

    private char[][] randomNextState()
    {
        char[][] result = new char[this.boardSize][];
        char[][] currState = saState.getState();
        for (int i=0; i<this.boardSize; i++) result[i] = currState[i].clone();

        int lizCount = rand.nextInt(numLizard);

        boolean done = false;
        for(int i = 0;i<this.boardSize;i++)
        {
            for(int j = 0;j<this.boardSize;j++)
            {
                if(currState[i][j] == '1'){
                    if(lizCount == 0) {

                        int randomI = rand.nextInt(boardSize);
                        int randomJ = rand.nextInt(boardSize);
                        while (result[randomI][randomJ] != '0') {
                            randomI = rand.nextInt(boardSize);
                            randomJ = rand.nextInt(boardSize);
                        }

                        result[randomI][randomJ] = '1';
                        result[i][j] = '0';
                        done = true;
                        break;
                    }
                    lizCount--;

                }
            }
            if(done) break;
        }
        return result;
    }

    private boolean update(double p)
    {
        return (Math.random()<=p);
    }

    private Node startSA()
    {
        //init random state
        this.saState.random(this.numLizard);
        int currVal = value(saState.getState()), nextVal, t = 2;
        double T, p;
        char[][] nextState;
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while((currTime-startTime)<280000)
        {
            T = schedule(t);
            System.out.print("\rIteration "+t+", T = "+T+"， val = "+currVal);

            if(currVal == 0) return this.saState;
            nextState = randomNextState();
            nextVal = value(nextState);

            currTime = System.currentTimeMillis();
            if(currVal <= nextVal){
                saState.setState(nextState);
                currVal = nextVal;
            }
            else {
                p = Math.exp(((double)(nextVal-currVal))/T);
                if(update(p) && (currTime-startTime)<270000) {
                    saState.setState(nextState);
                    currVal = nextVal;
                }
            }
            t+=1;
        }
        return null;
    }

    private Node startDFSOrBFS() {
        while (nodes.size() > 0) {
            try {
                Node curr = removeHead();
                if (curr.goalCheck(this.numLizard)) return curr;
                expand(curr);
            } catch (Exception e) {
                continue;
            }
        }
        return null;
    }

    public void printSearch() {
        System.out.println("\n=======================================");
        System.out.println("Method: " + this.method);
        System.out.println("BoardSize: " + this.boardSize);
        System.out.println("# of Lizards: " + this.numLizard);

        for (Node node : nodes) {
            node.printNode();
        }
    }

    private void expand(Node node) {
        char[][] currState = node.getState();
        boolean started = false;
        int i = 0, j;

        for(; i<currState.length; i++) {
            for (j = 0; j < currState[i].length; j++) {
                if(currState[i][j] == '0'){
                    Node temp = Node.makeNode(currState, node.getNumOfLizard(), i, j, true);
                    if(this.method==0) nodes.add(temp);
                    else nodes.add(0,temp);
                    break;
                }
            }
            break;
        }
        for(i = 0; i<currState.length; i++)
        {
            for (j = 0; j< currState[i].length; j++)
            {
                if(currState[i][j] == '2' && started) break;
                if(currState[i][j] == '0')
                {
                    started = true;
                    Node temp = Node.makeNode(currState, node.getNumOfLizard(), i, j, false);

                    if(this.method==0) nodes.add(temp);
                    else nodes.add(0,temp);
                }
            }
            if(started) break;
        }

    }
}
