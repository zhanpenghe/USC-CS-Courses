public class Minimax {


    private char[][] initialState;
    private int size;   // board size
    private int types;  // # of types of fruits
    private float time;

    public Minimax(char[][] initialState, int size, int types, float time) {
        this.initialState = initialState;
        this.size = size;
        this.types = types;
        this.time = time;
        System.out.println(this);
    }

    public String toString()
    {
        String result = "---\nSize: "+this.size+"*"+this.size+"\n# of types: "+this.types+"\nRemaining Time: "+this.time+"s\nBoard: \n";

        for(int i = 0; i<initialState.length; i++)
        {
            for(int j = 0; j<initialState[i].length; j++)
            {
                result+=(this.initialState[i][j]+"\t");
            }
            result+="\n";
        }
        return result;
    }


}
