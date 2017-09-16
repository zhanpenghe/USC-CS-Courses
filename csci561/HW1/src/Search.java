import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Search {

    List<Node> nodes = null;
    Set<Node> inQueue = null;
    Node saState = null;

    int method = -1;//0==BFS, 1==DFS
    int count = 0;
    int boardSize;
    int numLizard;

    public Search(Node node, int boardSize, int numLizard, int method)
    {
        this.boardSize = boardSize;
        this.numLizard = numLizard;
        this.method = method;
        if(this.method == 1||this.method ==0){
            nodes = new LinkedList<>();
            inQueue = new HashSet<>();
            nodes.add(node);
        }else if (this.method == 2)
        {
            saState = node;
        }
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

    private double schedule(int t)
    {
        return 0.8*1/Math.log((double) t);
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
        char[][] result = new char[this.boardSize][this.boardSize];
        int randomCount = 1+(int)(Math.random() * (this.numLizard));
        int randomI, randomJ;
        for(int i =0; i<this.boardSize;i++)
        {
            for(int j =0; j<this.boardSize; j++)
            {
                if(result[i][j] == '1') continue;
                if(this.saState.getState()[i][j] == '1')
                {
                    randomCount-=1;
                    if(randomCount == 0)
                    {
                        result[i][j] = '0';
                        while(true)
                        {
                            randomI = (int)(Math.random() * (this.boardSize));
                            randomJ = (int)(Math.random() * (this.boardSize));
                            if(this.saState.getState()[randomI][randomJ] == '0'){
                                result[randomI][randomJ] = '1';
                                break;
                            }
                        }
                    }else result[i][j] = this.saState.getState()[i][j];
                }
                else result[i][j] = this.saState.getState()[i][j];
            }
        }

        return result;
    }

    private boolean update(double p)
    {
        if(p<0.0 || p>1.0) return false;

        double temp = Math.random();
        System.out.print(", random: "+temp);
        return (temp<=p);
    }

    private Node startSA()
    {
        //init random state
        this.saState.random(this.numLizard);
        int currVal, nextVal, t = 2;
        double T, p;
        char[][] nextState;
        while(true)
        {
            T = schedule(t);
            System.out.print("\nIteration "+t+", T = "+T);

            if(T == 0 || Node.strongGoalCheck(this.saState, this.numLizard)) return this.saState;
            //saState.printNode();
            currVal = value(saState.getState());
            System.out.print(", value: "+currVal);
            nextState = randomNextState();
            nextVal = value(nextState);

            if(currVal < nextVal) saState.setState(nextState);
            else {
                p = Math.exp(((double)(nextVal-currVal))/T);
                System.out.print(", p = "+p);
                if(update(p)) {
                    saState.setState(nextState);
                    System.out.print(", Accepted bad state");
                }
            }

            t+=1;
        }
    }

    private Node startDFSOrBFS() {
        int i = 0;
        while (nodes.size() > 0) {
            try {
                Node curr = removeHead();
                if(curr.getNumOfLizard()>i){
                    count=0;
                    i++;
                }
                System.out.print("\rDone with "+i+", Count: "+count);
                count+=1;
                if (curr.goalCheck(this.numLizard)) return curr;

                expand(curr);
            } catch (Exception e) {
                continue;
            }
            //printSearch();
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
