public class Node implements Comparable<Node>{

    private int alpha;
    private int beta;
    private int score;
    private char[][] state;
    private char[][] tMap;

    public Node(int alpha, int beta, char[][] state) {
        this.alpha = alpha;
        this.beta = beta;
        this.state = state;
        this.tMap = new char[state.length][state.length];
        this.gravity();
        for(int i=0; i<state.length; i++) this.tMap[i] = this.state[i].clone();
        this.score = 0;
    }

    public char[][] gettMap() {
        return tMap;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBeta() {
        return beta;
    }

    public char[][] getState() {
        return state;
    }

    public int getScore() {
        return score;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void gravity() {
        int n, i, j;
        for(j = 0; j<state.length; j++)
        {
            n = state.length-1;
            for(i = state.length-1;i>=0;i--)
            {
                if(state[i][j] != '*' && state[i][j]!='T')
                {
                    state[n][j] = state[i][j];
                    n-=1;
                }
            }
            while(n>=0)
            {
                state[n][j]='*';
                n-=1;
            }
        }
    }

    public boolean isEmpty() {
        for(int i = 0;i<state.length; i++)
        {
            for (int j = 0; j<state.length; j++)
            {
                if(state[i][j] != '*') return false;
            }
        }
        return true;
    }

    public void printTMap() {
        for(int i = 0; i<state.length; i++)
        {
            for(int j = 0; j<state.length;j++) System.out.print(tMap[i][j]+"\t");
            System.out.println();
        }
    }

    public String toString() {
        String result = "\n-------------\nalpha:\t"+alpha+"\nbeta:\t"+beta+"\nscore:\t"+score+"\n\nBoard:\n";

        for(int i = 0; i<state.length; i++)
        {
            for(int j = 0; j<state.length;j++) result+=(state[i][j]+"\t");
            result+="\n";
        }

        return result;
    }

    public boolean prune() {
        return (this.alpha>=this.beta);
    }

    public void take(short i, short j)
    {
        this.tMap[i][j] = 'T';
    }

    @Override
    public int compareTo(Node o) {
        if(this.score > o.getScore()) return 1;
        else if(this.score < o.getScore()) return -1;
        else return 0;
    }
}
