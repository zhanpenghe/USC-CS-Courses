import com.sun.org.apache.xpath.internal.operations.Bool;

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

        int indexOfArgStart = str.indexOf("(");
        int indexOfArgEnd = str.indexOf(")");
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

    public AtomSentence(Predicate predicate, Boolean negate) {
        this.predicate = predicate;
        this.negate = negate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public Boolean getNegate() {
        return negate;
    }

    public boolean equals(Object obj){
        AtomSentence atom = (AtomSentence)obj;
        return (atom.getPredicate().equals(this.predicate) && this.negate == atom.negate);
    }

    public String toString(){
        if(negate) return "~"+predicate.toString();
        else return predicate.toString();
    }

    public static List<Term> searchUnification(List<List<Term>> unification, Term term){
        for(List<Term> uni: unification){
            try{
                if(uni.get(0).equals(term)) return uni;
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        return null;
    }

    private static void printUnification(List<List> unification, boolean successful, AtomSentence sentence1, AtomSentence sentence2, String errorMsg){
        System.out.println("Unification of "+sentence1+" and "+sentence2+": "+(successful?"Succeeded!":"Failed!"));
        if(!successful) System.out.println(errorMsg);
        System.out.print("{");
        for(int i=0; i<unification.size();i++){
            List terms = unification.get(i);
            System.out.print(terms.get(0)+"/"+terms.get(1));
            if(i<unification.size()-1) System.out.print(", ");
        }
        System.out.println("}");
    }

    public static Boolean unify(AtomSentence sentence1, AtomSentence sentence2, List unification){
        //check predicate type
        if(!sentence1.getPredicate().getName().equals(sentence2.getPredicate().getName()) || sentence1.getPredicate().getNumOfArgs()!=sentence2.getPredicate().getNumOfArgs()){
            //printUnification(unification, false, sentence1, sentence2, "Different predicate names.");
            return false;
        }

        //unify arguments
        for(int i = 0; i<sentence1.getPredicate().getNumOfArgs(); i++){
            if(!unify(sentence1.getPredicate().getArgument(i), sentence2.getPredicate().getArgument(i), unification)){
                //printUnification(unification, false, sentence1, sentence2, "Failed on unifying arguments:"+sentence1.getPredicate().getArgument(i)+" and "+sentence2.getPredicate().getArgument(i));
                return false;
            }
        }

        //printUnification(unification, true, sentence1, sentence2, null);
        return true;
    }

    private static Boolean unify(Term term1, Term term2, List unification){
        if(term1.equals(term2)) return true;
        if(term1.getType()==Term.CONSTANT && term2.getType()==Term.CONSTANT) return false;
        if(term1.getType()==Term.VARIABLE){
            if(searchUnification(unification, term1)!=null) return unify((Term)searchUnification(unification, term1).get(1), term2, unification);
            if(searchUnification(unification, term2)!=null) return unify(term1, (Term)searchUnification(unification, term2).get(1), unification);
            else{
                List<Term> temp = new ArrayList<>();
                temp.add(term1);
                temp.add(term2);
                unification.add(new ArrayList<>(temp));
                return true;
            }
        }else if(term2.getType()==Term.VARIABLE){
            if(searchUnification(unification, term2)!=null) return unify((Term)searchUnification(unification, term2).get(1), term1, unification);
            if(searchUnification(unification, term1)!=null) return unify(term2, (Term)searchUnification(unification, term1).get(1), unification);
            else{
                List<Term> temp = new ArrayList<>();
                temp.add(term2);
                temp.add(term1);
                unification.add(new ArrayList<>(temp));
                return true;
            }
        }
        return false;
    }
}
