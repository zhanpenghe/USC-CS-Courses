import java.util.List;

public class Predicate {

    private String name;
    private List<Term> arguments;
    private Integer numOfArgs;

    public Predicate(String name, Integer numOfVars, List<Term> variables){

        if(numOfVars!=variables.size()) return;

        this.name = name;
        this.numOfArgs = numOfVars;
        this.arguments = variables;
    }

    public String getName() {
        return name;
    }

    public List<Term> getArguments(){
        return arguments;
    }

    public Term getArgument(int i) {
        return arguments.get(i);
    }

    public Integer getNumOfArgs() {
        return numOfArgs;
    }

    public Boolean equals(Predicate pred){
        if(!this.name.equals(pred.getName()) || this.numOfArgs!=pred.getNumOfArgs()) return false;
        for(int i = 0; i<this.arguments.size(); i++){
            if(!pred.getArgument(i).equals(this.getArgument(i))) return false;
        }
        return true;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append("(");
        for(int i = 0; i<this.numOfArgs; i++){
            sb.append(arguments.get(i).getValue());
            sb.append(arguments.get(i).getType()==Term.CONSTANT?": CONSTANT":": VARIABLE");
            if(i<this.numOfArgs-1) sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }
}
