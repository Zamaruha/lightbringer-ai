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
        if (!clauses.contains(clause)) {
            clauses.add(clause);
        }
    }
    
    public void pushSingle(int literal, Board board, boolean optimize) {
        Clause clause = new Clause(board);
        clause.addLiteral(literal);
        if (optimize) {
            optimizeBase(literal, board);
        }
        this.push(clause);
    }
    
    private void optimizeBase(int target, Board board) {
        ArrayList<Clause> toRemove = new ArrayList<Clause>();
        ArrayList<Clause> toAdd = new ArrayList<Clause>();
        for (Clause clause : clauses) {
            ArrayList<Integer> allLiterals = clause.getLiterals();
            for (int literal : allLiterals) {
                if (target == literal) {
                    toRemove.add(clause);
                    break;
                } else if (target == -literal) {
                    toAdd.add(removeLiteral(literal, clause, board));
                    toRemove.add(clause);
                    break;
                }
            }
        }
        clauses.removeAll(toRemove);
        clauses.addAll(toAdd);
    }
    
    private Clause removeLiteral(int literal, Clause clause, Board board) {
        Clause newClause = new Clause(board);
        for (int newLiteral : clause.getLiterals()) {
            if (newLiteral != literal) {
                newClause.addLiteral(newLiteral);
            }
        }
        return newClause;
    }
    
    public void runCleanup(Board board) {
        for (Cell cell : board.getAllCells()) {
            if (cell.isBomb()) {
                pushSingle(cell.getId(), board, true);
            } else if (cell.isOpened()) {
                pushSingle(-cell.getId(), board, true);
            }
        }
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
