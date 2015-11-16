package hextech;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import api.MSAgent;
import api.MSField;

public class HextechAgent extends MSAgent {

    private KnowledgeBase base;
    private ArrayList<Cell> nextMoves = new ArrayList<Cell>();
    private Board board;
    private HextechSolver solver;
    private boolean logging;
    
    public HextechAgent(boolean logging) {
        this();
        this.logging = logging;
    }
    
    public HextechAgent() {
        this.base = new KnowledgeBase();
        solver = new HextechSolver();
    }
    
    @Override
    public void setField(MSField field) {
        super.setField(field);
        this.board = new Board(field.getNumOfRows(), field.getNumOfCols());
    }
    
    private void addNextMove(Cell cell) {
        if (!cell.isHidden()) {
            return;
        }
        nextMoves.add(cell);
    }
    
    private void addKnowledge(Cell cell) {
        if (cell.isBomb()) {
            // Add this as bomb to base
            Clause clause = new Clause(board);
            clause.addLiteral(cell.getId());
            base.push(clause);
            return;
        } else if (cell.isHidden()) {
            return;
        }
        
        // Add this as not bomb
        base.pushSingle(-cell.getId(), board);
        
        // Add the clauses for the neighbours
        ArrayList<Cell> neighbourCells = board.getNeighbours(cell);
        
        // Get the maximum
        populateKnowledgeBase(neighbourCells, cell.getValue() + 1, false);
        
        // Get the minimum
        int val = neighbourCells.size() - cell.getValue() + 1;
        populateKnowledgeBase(neighbourCells, val, true);
        
    }
    
    private void populateKnowledgeBase(ArrayList<Cell> cells, int maxLen, boolean isPositive){
        ArrayList<Cell> resList = new ArrayList<>();
        for (int i = 0; i < maxLen; i++) {
            resList.add(null);
        }
        fillBaseCombinations(cells, maxLen, 0, resList, maxLen, isPositive ? 1 : -1);
    }
    
    private void fillBaseCombinations(ArrayList<Cell> cells, int maxLen, int startPos, List<Cell> res, int startLen, int factor) {
        if (maxLen == 0) {
            Clause clause = new Clause(board);
            for (Cell cell : res) {
                clause.addLiteral(cell.getId() * factor);
            }
            base.push(clause);
            return;
        }
        for (int i = startPos; i < cells.size() - maxLen + 1; i++) {
            res.set(startLen - maxLen, cells.get(i));
            fillBaseCombinations(cells, maxLen - 1, i + 1, res, startLen, factor);
        }
    }

    
    @Override
    public boolean solve() {
        
        int numOfRows = field.getNumOfRows();
        int numOfCols = field.getNumOfCols();
        int maxVar = numOfRows * numOfCols + 1;
        Random random = new Random();
        int x = 0;
        int y = 0;
        int feedback = 0;
        
        do {
            Cell cell = null;
            if (this.nextMoves.size() > 0) {
                log("Making safe next move.");
                do {
                    cell = nextMoves.remove(nextMoves.size() - 1);
                } while (!cell.isHidden() && nextMoves.size() > 0);
                
                x = cell.getX();
                y = cell.getY();
            } else {
                log("We pick a random move.");
                do {
                    x = random.nextInt(numOfCols);
                    y = random.nextInt(numOfRows);
                    cell = board.getCell(x, y);
                } while (!cell.isHidden());
            }
            
            log("Uncovering (" + x + ", " + y + ")");
            feedback = field.uncover(x, y);
            cell.setOpen(feedback);
            board.setCell(x, y, cell);
            log(field.toString());
            log(board.toString());
            
            if (feedback == 0) {
                base.pushSingle(-cell.getId(), board);
                ArrayList<Cell> neighbourCells = board.getNeighbours(cell);
                for (Cell neighbourCell : neighbourCells) {
                    addNextMove(neighbourCell);
                }
            } else if (feedback == -1) {
                log("BOMB");
                break;
            } else {
                log("Adding knowledge from feedback");
                addKnowledge(cell);
            }

            for (int i = 2; i <= maxVar; i++) {
                if (nextMoves.size() > 0) {
                    break;
                }
                
                Cell testCell = board.findCellById(i);
                if (!testCell.isHidden()) {
                    continue;
                }
                
                log(board.toString());

                
                log("Sjekker safespot på " + testCell.getName());
                base.pushSingle(testCell.getId(), board);                    

                Boolean isSatisfiable = solver.ask(base, maxVar);
                base.pop();
                if (isSatisfiable == null) {
                    log("Safe field contra on: " + i + " (" + testCell.getName() + ")\n");
                    base.pushSingle(testCell.getId() * -1, board);
                    nextMoves.add(testCell);
                    break;
                }
                else if (!isSatisfiable) {
                    log("Found a safe cell in " + i + "(" + testCell.getX() + ", " + testCell.getY() + ")");
                    nextMoves.add(testCell);
                    continue;
                } else {
                    log("We did not find any safe cells.");
                }
                
                // Check for bombs
                log("Sjekker bomber på " + testCell.getName());
                base.pushSingle(testCell.getId() * -1, board);                    
                isSatisfiable = solver.ask(base, maxVar);
                base.pop();
                if (isSatisfiable == null) {
                    log("Bomb field contra on: " + i + " (" + testCell.getName() + ")\n");
                    base.pushSingle(testCell.getId(), board);
                    testCell.setBomb();
                    i = 1;
                    continue;
                }
                else if (!isSatisfiable) {
                    log("Found a bomb cell in " + i + "(" + testCell.getX() + ", " + testCell.getY() + ")");
                    testCell.setBomb();
                    base.pushSingle(testCell.getId(), board);
                    i = 1;
                    continue;
                } else {
                    log("We did not find any bomb cells.");
                }
            }
            
            
        } while (feedback > -1 && !field.solved());

        return field.solved();
    }
    
    private void log(String message) {
        if (this.logging) {
            System.out.println(message);
        }
    }

}
