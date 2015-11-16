package hextech;

import java.util.ArrayList;

import api.Utils;

public class Clause {

    private ArrayList<Integer> literals;
    private Board board;
    
    public Clause(Board board) {
        this.board = board;
        this.literals = new ArrayList<Integer>();
    }
    
    public void addLiteral(int value) {
        if (value == 0) {
            return;
        }
        if (!literals.contains(value)) {
            literals.add(value);
        }
    }
    
    public ArrayList<Integer> getLiterals() {
        return literals;
    }
    
    public String toString(){
        String res = "";
        for (int literal : literals) {
            Cell cell = board.findCellById(Math.abs(literal));
            res += literal < 0 ? "!" : "";
            res += cell.getName() + " v ";
        }
        if (res.length() < 3) {
            return res;
        }
        return res.substring(0, res.length() - 3);
    }
    
    public int size() {
        return literals.size();
    }
    
    public int[] toArray() {
        return Utils.listToArray(literals);
    }
    
    @Override
    public boolean equals(Object other) {
        boolean equal = false;
        if (other instanceof Clause) {
            Clause otherClause = (Clause) other;
            return this.literals.equals(otherClause.getLiterals());
        }
        return equal;
        
    }
}
