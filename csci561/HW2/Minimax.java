import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Minimax {

    private int size;   // board size
    private int types;  // # of types of fruits
    private double time;
    private long startTime;

    private Node root;

    public Minimax(char[][] initialState, int size, int types, float time) {
        root = new Node(Integer.MIN_VALUE,Integer.MAX_VALUE,initialState);
        this.size = size;
        this.types = types;
        this.time = time*1000.0;
        System.out.println(this);
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String run() {
        if(root.isEmpty()) return null;
        if(noTime(1000)) return randomMove();

        short i, j;
        char[][] tMap = root.gettMap();
        Node optimalSolution = null;
        short optimalI = -1;
        short optimalJ = -1;

        for(i = 0; i<size; i++)
        {
            for(j = 0; j<size; j++) {
                if (tMap[i][j] != '*' && tMap[i][j] != 'T') {
                    System.out.println(root);
                    root.printTMap();
                    Node temp = crawl(root, i, j, true);
                    System.out.println(temp);
                    temp.printTMap();
                    root.setAlpha(Math.max(root.getAlpha(), minValue(temp, 1)));
                    if (optimalSolution == null){
                        optimalSolution = temp;
                        optimalI = i;
                        optimalJ = j;
                    }
                    else if (optimalSolution.getAlpha() < temp.getBeta()){
                        optimalSolution = temp;
                        optimalI = i;
                        optimalJ = j;
                    }
                }
            }
        }

        return translate(optimalSolution, optimalI, optimalJ);
    }

    private String translate(Node node, short i, short j)
    {
        if(i==-1||j==-1){
            System.out.println("[ERROR] Error when looking for optimal solution and return a dummy random move now.");
            return randomMove();
        }

        StringBuilder sb = new StringBuilder();
        sb.append((char)('A'+j));
        sb.append((j+1));

        char[][] state = node.getState();
        for(i = 0; i<size; i++)
        {
            sb.append('\n');
            for(j = 0; j<size; j++) {
                sb.append(state[i][j]);
            }
        }
        return sb.toString();

    }

    private String randomMove()
    {
        char[][] state = root.getState();
        for(short i = 0; i<size; i++)
        {
            for(short j = 0; j<size; j++)
            {
                if(state[i][j]!='*')
                {
                    getVal(state, i, j, state);
                    return translate(new Node(0, 0 , state), i, j);
                }
            }
        }
        return null;

    }

    private int maxValue(Node node, int depth) {
        System.out.println("*********************MAX***********************");
        if(noTime(1000) || node.isEmpty() || depth>5) return eval(node);

        short i, j;
        char[][] tMap = node.gettMap();

        for(i = 0; i<size; i++) {
            for (j = 0; j < size; j++) {
                if(tMap[i][j]!='*' && tMap[i][j]!='T'){
                    System.out.println(node);
                    node.printTMap();
                    Node temp = crawl(node, i, j, true);
                    System.out.println(temp);
                    temp.printTMap();
                    node.setAlpha(Math.max(node.getAlpha(), minValue(temp, depth+1)));
                    if(node.prune()) return node.getBeta();
                }
            }
        }
        return node.getAlpha();
    }

    private int minValue(Node node, int depth) {
        System.out.println("*********************MIN***********************");
        if(noTime(1000) || node.isEmpty() || depth>10) return eval(node);

        short i, j;
        char[][] tMap = node.gettMap();

        for(i = 0; i<size; i++) {
            for (j = 0; j < size; j++) {
                if(tMap[i][j]!='*' && tMap[i][j]!='T'){

                    System.out.println(node);
                    node.printTMap();
                    Node temp = crawl(node, i, j, false);
                    System.out.println(temp);
                    temp.printTMap();
                    node.setBeta(Math.min(node.getBeta(), maxValue(temp, depth+1)));
                    if(node.prune()) return node.getAlpha();
                }
            }
        }
        return node.getBeta();
    }

    /**
     *
     * @param node  The current node
     * @param i x coordinate
     * @param j y coordinate
     * @param isMyTurn  false if it's opponent's turn
     * @return A successor node
     */
    private Node crawl(Node node, short i, short j, boolean isMyTurn) {
        //copy the array
        System.out.println("\nCrawling from ("+i+", "+j+")");
        char[][] state = node.getState();
        char[][] tMap = node.gettMap();

        char[][] result = new char[size][size];
        for (int m = 0; m<size; m++) result[m] = state[m].clone();

        List<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));

        char target = state[i][j];
        node.take(i, j);
        result[i][j] = '*';

        int val = 1;
        Point temp;
        while(!queue.isEmpty())
        {
            temp = queue.remove(0);
            try{
                if(temp.i-1 >= 0)
                {
                    if(tMap[temp.i-1][temp.j]==target)
                    {
                        node.take((short)(temp.i-1), temp.j);
                        result[temp.i-1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i-1), temp.j));
                        val+=1;
                    }
                }
                if(temp.i+1 < size)
                {
                    if(tMap[temp.i+1][temp.j]==target)
                    {
                        node.take((short)(temp.i+1), temp.j);
                        result[temp.i+1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i+1), temp.j));
                        val+=1;
                    }
                }
                if(temp.j-1 >= 0)
                {
                    if(tMap[temp.i][temp.j-1]==target)
                    {
                        node.take(temp.i, (short)(temp.j-1));
                        result[temp.i][temp.j-1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j-1)));
                        val+=1;
                    }
                }
                if(temp.j+1 < size)
                {
                    if(tMap[temp.i][temp.j+1]==target)
                    {
                        node.take(temp.i, (short)(temp.j+1));
                        result[temp.i][temp.j+1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j+1)));
                        val+=1;
                    }
                }

            }catch(Exception e)
            {
                e.printStackTrace();
                continue;
            }
        }

        Node resultNode = new Node(node.getAlpha(), node.getBeta(), result);

        if(isMyTurn) resultNode.setScore(val+node.getScore());
        else resultNode.setScore(node.getScore()-val);
        return resultNode;
    }

    public String toString() {
        String result = "---\nSize: "+this.size+"*"+this.size+"\n# of types: "+this.types+"\nRemaining Time: "+(int)this.time+"ms\n";
        return result;
    }

    /**
     * Calculate the expected score
     * @param node current state node
     * @return expected score of current state
     */
    private int eval(Node node)
    {
        if(node.isEmpty()) return node.getScore();

        char[][] tMap = node.gettMap();
        int greatest = 0;
        int secondGreatest = 0;

        for (short i = 0; i < size; i++) {
            for (short j = 0;j<size; j++){
                if (tMap[i][j]!='*' && tMap[i][j]!='T'){

                    char[][] tempMap = new char[size][size];
                    for (int m = 0; m<size; m++) tempMap[m] = node.getState()[m].clone();

                    int val = getVal(node.getState(), i, j, tempMap);

                    if(val > greatest)
                    {
                        secondGreatest = greatest;
                        greatest =  val;
                    }else if(val > secondGreatest)
                    {
                        secondGreatest = val;
                    }

                }
            }
        }
        int result = node.getScore()+greatest-secondGreatest;
        node.setAlpha(result);
        node.setBeta(result);
        return result;
    }

    private int getVal(char[][] state, short i, short j, char[][] tempMap) {
        char target = state[i][j];

        List<Point> queue = new ArrayList<>();
        queue.add(new Point(i, j));

        int val = 1;
        Point temp;
        while(!queue.isEmpty())
        {
            temp = queue.remove(0);
            try{
                if(temp.i-1 >= 0)
                {
                    if(tempMap[temp.i-1][temp.j]==target) {
                        tempMap[temp.i-1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i-1), temp.j));
                        val+=1;
                    }
                }
                if(temp.i+1 < size)
                {
                    if(tempMap[temp.i+1][temp.j]==target){
                        tempMap[temp.i+1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i+1), temp.j));
                        val+=1;
                    }
                }
                if(temp.j-1 >= 0)
                {
                    if(tempMap[temp.i][temp.j-1]==target)
                    {
                        tempMap[temp.i][temp.j-1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j-1)));
                        val+=1;
                    }
                }
                if(temp.j+1 < size)
                {
                    if(tempMap[temp.i][temp.j+1]==target)
                    {
                        tempMap[temp.i][temp.j+1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j+1)));
                        val+=1;
                    }
                }

            }catch(Exception e)
            {
                e.printStackTrace();
                continue;
            }
        }
        return val;
    }

    private boolean noTime(double offset)
    {
        if(offset == 3)
        {
            System.out.println(time);
        }
        //System.out.println(System.currentTimeMillis());
        return (double)(System.currentTimeMillis() - startTime) > (time-offset);
    }
}
