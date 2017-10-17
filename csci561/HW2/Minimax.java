import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;

public class Minimax {

    private int size;   // board size
    private int types;  // # of types of fruits
    private double time;
    private long startTime;
    private int maxDepth;

    private Node root;

    public Minimax(char[][] initialState, int size, int types, float time) {
        root = new Node(Integer.MIN_VALUE,Integer.MAX_VALUE,initialState);
        this.size = size;
        this.types = types;
        this.time = time*1e9;
        this.maxDepth = 0;
        if(this.size<=8 && this.time>2e9) this.maxDepth=5;
        //System.out.println(this.maxDepth);
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String run() {

        if(root.isEmpty()) return null;
        if(noTime(1e9)) return randomMove();

        short i, j;

        char[][] tMap = new char[size][size];
        for(i=0; i<size; i++) tMap[i] = root.getState()[i].clone();
        Node optimalSolution = null;

        List<Node> successors = new ArrayList<>();
        Map<Node, Point> positions = new HashMap<>();
        for(i = 0; i<size; i++) {
            for (j = 0; j < size; j++) {
                if (tMap[i][j] != '*' && tMap[i][j] != 'T') {
                    Node temp = crawl(root, tMap, i, j, true);
                    successors.add(temp);
                    positions.put(temp, new Point(i, j));
                }
            }
        }

        Collections.sort(successors, Collections.reverseOrder());

        for(Node successor: successors){
            if(noTime(2e9)){
                if(optimalSolution == null) optimalSolution = successors.get(0);
                return translate(optimalSolution, positions.get(optimalSolution).i, positions.get(optimalSolution).j);
            }

            root.setAlpha(Math.max(root.getAlpha(), minValue(successor, 1)));

            if(optimalSolution == null) optimalSolution = successor;
            else if(optimalSolution.getBeta()<successor.getBeta()) optimalSolution = successor;

        }

        System.out.println(optimalSolution);

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
            for(short j = 0; j<size; j++) {
                if(state[i][j]!='*') {
                    getVal(state, i, j, state);
                    return translate(new Node(0, 0 , state), i, j);
                }
            }
        }
        return null;
    }

    private int maxValue(Node node, int depth) {
        if(node.isEmpty()){
            node.setAlpha(node.getScore());
            node.setBeta(node.getScore());
            return node.getScore();
        }
        if(noTime(3e9) || depth>this.maxDepth){
            long st = getTime();
            int temp =  eval2(node, true, 10);
            //System.out.println((getTime()-st)/1000000+"ms");
            return temp;
        }
        short i, j;
        char[][] tMap = new char[size][size];
        for(i=0; i<size; i++) tMap[i] = node.getState()[i].clone();

        List<Node> successors = new ArrayList<>();

        for(i = 0; i<size; i++) {
            for(j = 0; j<size; j++){
                if(tMap[i][j] != '*' && tMap[i][j] != 'T'){
                    successors.add(crawl(node, tMap, i, j, true));
                }
            }
        }

        Collections.sort(successors, Collections.reverseOrder());

        for(Node successor: successors){
            //System.out.println("=====MAX====\nDepth: "+depth+"\n"+node+"\n-----\n"+successor+"\n");
            node.setAlpha(Math.max(node.getAlpha(), minValue(successor, depth+1)));
            if(noTime(2e9)) break;
            //System.out.println(node.getAlpha()+">="+node.getBeta()+": "+(node.getAlpha()>=node.getBeta()));
            if(node.prune()) return node.getBeta();
        }
        return node.getAlpha();
    }

    private int minValue(Node node, int depth) {

        if(node.isEmpty()){
            node.setAlpha(node.getScore());
            node.setBeta(node.getScore());
            return node.getScore();
        }
        if(noTime(3e10)  || depth>this.maxDepth){
            long st = getTime();
            int temp =  eval2(node, false, 10);
            //System.out.println((getTime()-st)/1000000+"ms");
            return temp;
        }

        short i, j;
        char[][] tMap = new char[size][size];
        for(i=0; i<size; i++) tMap[i] = node.getState()[i].clone();

        List<Node> successors = new ArrayList<>();
        for(i = 0; i<size; i++)
        {
            for(j = 0; j<size; j++){
                if(tMap[i][j] != '*' && tMap[i][j] != 'T') {
                    successors.add(crawl(node, tMap, i, j, false));
                }
            }
        }
        Collections.sort(successors);

        for(Node successor: successors){
            node.setBeta(Math.min(node.getBeta(), maxValue(successor, depth+1)));
            if(noTime(2e9)) break;
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
    private Node crawl(Node node, char[][] tMap, short i, short j, boolean isMyTurn) {

        char[][] state = node.getState();

        char[][] result = new char[size][size];
        for (int m = 0; m<size; m++) result[m] = state[m].clone();

        List<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));

        char target = state[i][j];
        tMap[i][j] = 'T';
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
                        tMap[(short)(temp.i-1)][temp.j] = 'T';
                        result[temp.i-1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i-1), temp.j));
                        val+=1;
                    }
                }
                if(temp.i+1 < size)
                {
                    if(tMap[temp.i+1][temp.j]==target)
                    {
                        tMap[(short)(temp.i+1)][temp.j] = 'T';
                        result[temp.i+1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i+1), temp.j));
                        val+=1;
                    }
                }
                if(temp.j-1 >= 0)
                {
                    if(tMap[temp.i][temp.j-1]==target)
                    {
                        tMap[temp.i][(short)(temp.j-1)]='T';
                        result[temp.i][temp.j-1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j-1)));
                        val+=1;
                    }
                }
                if(temp.j+1 < size)
                {
                    if(tMap[temp.i][temp.j+1]==target)
                    {
                        tMap[temp.i][(short)(temp.j+1)] = 'T';
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

    private int getVal(char[][] state, short i, short j, char[][] tempMap) {
        char target = state[i][j];

        List<Point> queue = new LinkedList<>();
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
        return (getTime() - startTime) > (time-offset);
    }

    public static long getTime() {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        return bean.isCurrentThreadCpuTimeSupported()?bean.getCurrentThreadCpuTime():0l;
    }

    private int eval2(Node node, boolean isMyTurn, int depth) {
        if(depth <= 0) return node.getScore();
        short i, j;

        List<Node> list = new LinkedList<>();
        char[][] tMap = new char[size][size];
        for(i=0; i<size; i++) tMap[i] = node.getState()[i].clone();

        for(i = 0; i<size; i++)
        {
            for(j = 0; j<size; j++)
            {
                if(tMap[i][j]!='*' && tMap[i][j]!='T') list.add(crawl(node, tMap, i, j, isMyTurn));
            }
        }

        if(list.size()==0){
            node.setAlpha(node.getScore());
            node.setBeta(node.getScore());
            return node.getScore();
        }
        Node opt = list.get(0);

        for(Node s: list){
            if(isMyTurn){
                if(s.getScore() > opt.getScore()) opt = s;
            }else{
                if(s.getScore() < opt.getScore()) opt = s;
            }
        }
        int result = eval2(opt, !isMyTurn, depth-1);
        node.setBeta(result);
        node.setAlpha(result);
        //System.out.println(node);
        return result;
    }
}
