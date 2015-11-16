package hextech;

import java.util.ArrayList;
import java.util.Arrays;

import org.sat4j.core.VecInt;

public class KnowledgeBase {

    ArrayList<Clause> clauses;
    
    public KnowledgeBase() {
        clauses = new ArrayList<Clause>();
    }
    
    public void push(Clause clause) {
        //System.out.println("Pushing: " + clause);
        if (!clauses.contains(clause)) {
            clauses.add(clause);
        }
    }
    
    public void pushSingle(int literal, Board board) {
        Clause clause = new Clause(board);
        clause.addLiteral(literal);
        this.push(clause);
    }
    
    public Clause pop() {
        return clauses.remove(clauses.size() - 1);
    }
    
    public int size() {
        return clauses.size();
    }
    
    public int[] getClauseArray(int index) {
        Clause clause = clauses.get(index);
        int[] arr = new int[clause.size()];
        arr = clause.toArray();
        return arr;
        
    }
    
    public VecInt getVecInt(int index) {
        //System.out.println("VecInt Array: " + Arrays.toString(getClauseArray(index)));
        return new VecInt(getClauseArray(index));
    }
    
    @Override
    public String toString() {
        String res = "";
        for (Clause clause : clauses) {
            res += clause + "\n";
        }
        return res;
    }
    
}
