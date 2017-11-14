public class Term {

    public static final Integer CONSTANT = 0;
    public static final Integer VARIABLE = 1;

    public static final Integer STRING_VAR = 0;
    public static final Integer NUM_VAR = 1;


    private Integer type;
    private Integer valueType;
    private String value;

    public Term(Integer type, String value, Integer valueType){

        if(type!=CONSTANT && type!=VARIABLE) return;

        this.type = type;
        this.value = value;
        this.valueType = valueType;
    }

    public Term(Term anotherTerm){
        this.type = anotherTerm.getType();
        this.value = anotherTerm.getValue();
        this.valueType = anotherTerm.getValueType();
    }

    public Integer getType() {
        return type;
    }

    public Integer getValueType() {
        return valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public Boolean equals(Term term){
        return (this.valueType==term.valueType && this.value.equals(term.getValue()) && this.type==term.type);
    }

    public String toString(){
        return this.value;
    }
}
