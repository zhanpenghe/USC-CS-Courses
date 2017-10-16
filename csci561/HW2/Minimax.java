import java.util.*;

public class Minimax {

    private int size;   // board size
    private int types;  // # of types of fruits
    private double time;
    private long startTime;
    private int maxDepth = 4;

    private Node root;

    public Minimax(char[][] initialState, int size, int types, float time) {
        root = new Node(Integer.MIN_VALUE,Integer.MAX_VALUE,initialState);
        this.size = size;
        this.types = types;
        this.time = time*1000.0;
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

        List<Node> successors = new ArrayList<>();
        Map<Node, Point> positions = new HashMap<>();
        for(i = 0; i<size; i++) {
            for (j = 0; j < size; j++) {
                if (tMap[i][j] != '*' && tMap[i][j] != 'T') {
                    Node temp = crawl(root, i, j, true);
                    successors.add(temp);
                    positions.put(temp, new Point(i, j));
                }
            }
        }

        Collections.sort(successors, Collections.reverseOrder());

        for(Node successor: successors){
            if(noTime(2000)){
                if(optimalSolution == null) optimalSolution = successors.get(0);
                return translate(optimalSolution, positions.get(optimalSolution).i, positions.get(optimalSolution).j);
            }

            root.setAlpha(Math.max(root.getAlpha(), minValue(successor, 1)));

            if(optimalSolution == null) optimalSolution = successor;
            else if(optimalSolution.getAlpha()<successor.getBeta()) optimalSolution = successor;
        }

        return translate(optimalSolution, positions.get(optimalSolution).i, positions.get(optimalSolution).j);
    }

    private String translate(Node node, short i, short j) {
        if(i==-1||j==-1){
            System.out.println("[ERROR] Error when looking for optimal solution and return a dummy random move now.");
            return randomMove();
        }

        StringBuilder sb = new StringBuilder();
        sb.append((char)('A'+j));
        sb.append((i+1));

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

    private String randomMove() {
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

        if(noTime(4000) || node.isEmpty() || depth>this.maxDepth) return eval(node, true);

        short i, j;
        char[][] tMap = node.gettMap();

        List<Node> successors = new ArrayList<>();

        for(i = 0; i<size; i++)
        {
            for(j = 0; j<size; j++){
                if(tMap[i][j] != '*' && tMap[i][j] != 'T') successors.add(crawl(node, i, j, true));
            }
        }

        Collections.sort(successors, Collections.reverseOrder());

        for(Node successor: successors){
            //System.out.println("=====MAX====\nDepth: "+depth+"\n"+node+"\n-----\n"+successor+"\n");
            node.setAlpha(Math.max(node.getAlpha(), minValue(successor, depth+1)));
            if(successor.prune()) return node.getBeta();
        }
        return node.getAlpha();
    }

    private int minValue(Node node, int depth) {

        if(noTime(3000) || node.isEmpty() || depth>this.maxDepth) return eval(node, false);

        short i, j;
        char[][] tMap = node.gettMap();
        List<Node> successors = new ArrayList<>();
        for(i = 0; i<size; i++)
        {
            for(j = 0; j<size; j++){
                if(tMap[i][j] != '*' && tMap[i][j] != 'T') successors.add(crawl(node, i, j, false));
            }
        }
        Collections.sort(successors);

        for(Node successor: successors){
            //System.out.println("=====MIN====\nDepth: "+depth+"\n"+node+"\n---\n"+successor+"\n");
            node.setBeta(Math.min(node.getBeta(), maxValue(successor, depth+1)));
            if(node.prune()) return node.getAlpha();
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

        if(isMyTurn) resultNode.setScore(node.getScore()+val*val);
        else resultNode.setScore(node.getScore()-val*val);
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
    private int eval(Node node, boolean isMyTurn) {
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
        int result;
        if(isMyTurn) result = node.getScore()+greatest;
        else result = node.getScore()-greatest;

        node.setAlpha(result);
        node.setBeta(result);
        return result;
    }

    private int getVal(char[][] state, short i, short j, char[][] tempMap) {
        char target = state[i][j];

        List<Point> queue = new ArrayList<>();
        queue.add(new Point(i, j));

        int val = 1;
        tempMap[i][j] = '*';
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
        return val*val;
    }

    private boolean noTime(double offset) {
        return (double)(System.currentTimeMillis() - startTime) > (time-offset);
    }
}
