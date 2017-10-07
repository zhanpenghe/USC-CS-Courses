public class Node {

    private int alpha;
    private int beta;
    private char[][] state;

    public Node(int alpha,int beta, char[][] state)
    {
        this.alpha = alpha;
        this.beta = beta;
        this.state = state;
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

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

    public void setBeta(int beta) {
        this.beta = beta;
    }

    public void setState(char[][] state) {
        this.state = state;
    }

    public void gravity()
    {
        int n, i, j;
        for(j = 0; j<state.length; j++)
        {
            n = state.length-1;
            for(i = state.length-1;i>=0;i--)
            {
                if(state[i][j] != '*')
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

    public String toString()
    {
        String result = "";
        for(int i = 0; i<state.length; i++)
        {
            for(int j = 0; j<state.length;j++)
            {
                result+=(state[i][j]+"\t");
            }
            result+="\n";
        }
        return result;
    }
}
