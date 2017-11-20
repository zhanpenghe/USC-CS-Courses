public class Term {

    public static final Integer CONSTANT = 0;
    public static final Integer VARIABLE = 1;

    private int type;
    private String value;

    public Term(int type, String value){

        if(type!=CONSTANT && type!=VARIABLE) return;

        this.type = type;
        this.value = value;
    }

    public Term(Term anotherTerm){
        this.type = anotherTerm.getType();
        this.value = new String(anotherTerm.getValue());
    }

    public int getType() {
        return type;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public Boolean equals(Term term){
        return (this.value.equals(term.getValue()) && this.type==term.type);
    }

    public String toString(){
        return this.value;
    }

    public Term clone(){
        return new Term(this.type, new String(this.value));
    }
}
