import java.util.LinkedList;
import java.util.List;

public class Minimax {

    private int size;   // board size
    private int types;  // # of types of fruits
    private float time;

    private Node root;

    public Minimax(char[][] initialState, int size, int types, float time) {
        root = new Node(Integer.MIN_VALUE,Integer.MAX_VALUE,initialState);
        this.size = size;
        this.types = types;
        this.time = time;
        System.out.println(this);
    }

    public void run()
    {
        maxValue(root);
    }

    private int maxValue(Node node)
    {
        System.out.println("*********************MAX***********************");
        if(node.isEmpty()) return node.getValue();

        short i, j;
        char[][] tMap = node.gettMap();

        for(i = 0; i<size; i++) {
            for (j = 0; j < size; j++) {
                if(tMap[i][j]!='*' && tMap[i][j]!='T'){
                    System.out.println(node);
                    node.printTMap();
                    Node temp = crawl(node, i, j);
                    node.setAlpha(Math.max(node.getAlpha(), minValue(temp)));
                    if(node.prune()) return node.getBeta();
                }
            }
        }
        return node.getAlpha();
    }

    private int minValue(Node node){
        System.out.println("*********************MIN***********************");
        if(node.isEmpty()) return node.getValue();

        short i, j;
        char[][] tMap = node.gettMap();

        for(i = 0; i<size; i++) {
            for (j = 0; j < size; j++) {
                if(tMap[i][j]!='*' && tMap[i][j]!='T'){

                    System.out.println(node);
                    node.printTMap();
                    Node temp = crawl(node, i, j);
                    node.setBeta(Math.min(node.getBeta(), maxValue(temp)));
                    if(node.prune()) return node.getAlpha();
                }
            }
        }
        return node.getBeta();
    }

    private Node crawl(Node node, short i, short j)
    {
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

        int val = node.getValue()+1;
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

        resultNode.setValue(val);

        System.out.println("..\n"+resultNode);
        resultNode.printTMap();

        return resultNode;
    }

    public String toString()
    {
        String result = "---\nSize: "+this.size+"*"+this.size+"\n# of types: "+this.types+"\nRemaining Time: "+this.time+"s\nBoard: \n";
        return result;
    }
}
