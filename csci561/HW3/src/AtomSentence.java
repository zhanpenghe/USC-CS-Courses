import java.util.ArrayList;
import java.util.List;

public class AtomSentence {

    private Predicate predicate;
    private Boolean negate;  //1 is negate otherwise it's not negated

    public AtomSentence(String str){

        int start = 0;
        if(str.charAt(0)=='~'){
            this.negate = true;
            start = 1;
        }else this.negate = false;

        int indexOfArgStart = str.indexOf('(');
        int indexOfArgEnd = str.indexOf(')');
        String name = str.substring(start, indexOfArgStart).trim();
        String[] args = str.substring(indexOfArgStart+1, indexOfArgEnd).split(",");
        List<Term> terms = new ArrayList<>();

        for(int i =0; i<args.length; i++){
            String arg = args[i].trim();
            if(arg.charAt(0)<='Z' && arg.charAt(0)>='A') terms.add(new Term(Term.CONSTANT, arg, Term.STRING_VAR));
            else terms.add(new Term(Term.VARIABLE, arg, Term.STRING_VAR));
        }

        this.predicate = new Predicate(name, terms.size(), terms);
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Boolean getNegate() {
        return negate;
    }

    public Boolean equals(AtomSentence atom){
        return (atom.getPredicate().equals(this.predicate) && this.negate == atom.negate);
    }

    public String toString(){
        if(negate) return "~"+predicate.toString();
        else return predicate.toString();
    }

    public static void unify(AtomSentence sentence1, AtomSentence sentence2, List unification){
        if(!sentence1.getPredicate().getName().equals(sentence2.getPredicate().getName())) return;
        
    }
}
