import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class Minimax {

    private int size;   // board size
    private int types;  // # of types of fruits
    private float time;

    private Stack<Node> stack;

    public Minimax(char[][] initialState, int size, int types, float time) {
        this.stack = new Stack<>();
        stack.push(new Node(1,2,initialState));
        this.size = size;
        this.types = types;
        this.time = time;
        System.out.println(this);
    }

    public void testExpand()
    {
        Node temp = stack.pop();
        expand(temp);
    }

    private void expand(Node node){

        char[][] state = node.getState();
        printMatirx(state);
        short i, j;

        for(i = 0; i<size; i++)
        {
            for(j = 0; j<size; j++)
            {
                if(state[i][j]!='*' && state[i][j]!='T'){
                    Node temp = crawl(state, i, j);
                    System.out.println(temp);
                    printMatirx(state);
                    stack.push(temp);
                }
            }
        }
    }

    private void printMatirx(char[][] arr)
    {
        System.out.println();
        for(int i = 0; i<size; i++)
        {
            for(int j = 0; j<size; j++)
            {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
    }
    private Node crawl(char[][] state, short i, short j)
    {
        //copy the array
        char[][] result = new char[size][size];
        for (int m = 0; m<size; m++) result[m] = state[m].clone();

        List<Point> queue = new LinkedList<>();
        queue.add(new Point(i, j));

        char target = state[i][j];
        state[i][j] = 'T';
        result[i][j] = '*';
        Point temp;
        while(!queue.isEmpty())
        {
            temp =  queue.remove(0);
            try{
                if(temp.i-1 >= 0)
                {
                    if(state[temp.i-1][temp.j]==target)
                    {
                        state[temp.i-1][temp.j] = 'T';
                        result[temp.i-1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i-1), temp.j));
                    }
                }
                if(temp.i+1 < size)
                {
                    if(state[temp.i+1][temp.j]==target)
                    {
                        state[temp.i+1][temp.j] = 'T';
                        result[temp.i+1][temp.j] = '*';
                        queue.add(new Point((short)(temp.i+1), temp.j));
                    }
                }
                if(temp.j-1 >= 0)
                {
                    if(state[temp.i][temp.j-1]==target)
                    {
                        state[temp.i][temp.j-1] = 'T';
                        result[temp.i][temp.j-1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j-1)));
                    }
                }
                if(temp.j+1 < size)
                {
                    if(state[temp.i][temp.j+1]==target)
                    {
                        state[temp.i][temp.j+1] = 'T';
                        result[temp.i][temp.j+1] = '*';
                        queue.add(new Point(temp.i, (short)(temp.j+1)));
                    }
                }

            }catch(Exception e)
            {
                e.printStackTrace();
                continue;
            }
        }

        Node resultNode = new Node(1,2,result);
        resultNode.gravity();
        return resultNode;
    }

    public String toString()
    {
        String result = "---\nSize: "+this.size+"*"+this.size+"\n# of types: "+this.types+"\nRemaining Time: "+this.time+"s\nBoard: \n";

        /*for(int i = 0; i<initialState.length; i++)
        {
            for(int j = 0; j<initialState[i].length; j++)
            {
                result+=(this.initialState[i][j]+"\t");
            }
            result+="\n";
        }*/
        return result;
    }


}
