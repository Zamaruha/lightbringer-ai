package api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;

public class AgentOfLight extends MSAgent {
    private Random rand;
    
    int MAXVAR;

    // -1 = confirmed bomb, 0 = unknown, 1 = confirmed safe/uncovered
    int[] board;

    ISolver knowledgeBase = new ModelIterator(SolverFactory.newDefault());

    private void addKnowledge(int value, int x, int y) throws ContradictionException {
        int intVal = this.getFieldFromCoordinate(x, y);
        knowledgeBase.addClause(new VecInt(new int[] { intVal * -1 }));
        if (value == 0) {
            return;
        }
        
        ArrayList<Integer> neighbours = getNeighbours(x, y);

        // Get the maximum
        int[] combos = Utils.listToArray(neighbours);
        populateKB(combos, value + 1, 0, new int[value + 1], -1);

        // Get the minimum
        int count = neighbours.size() - value + 1;
        populateKB(combos, count, 0, new int[count], 1);
        
    }

    private void populateKB(int[] arr, int len, int startPosition, int[] result, int scalar)
            throws ContradictionException {
        if (len == 0) {
            //System.out.println("Base: " + Arrays.toString(Utils.multiply(result, scalar)));
            knowledgeBase.addClause(new VecInt(Utils.multiply(result, scalar)));
            return;
        }
        for (int i = startPosition; i <= arr.length - len; i++) {
            result[result.length - len] = arr[i];
            populateKB(arr, len - 1, i + 1, result, scalar);
        }
    }

    private int[] askKB() {
        int[] results = new int[MAXVAR + 1];
        try {
            for(int i = 1; i <= MAXVAR; i++) {
                // unsatisfiable with field i as a bomb, conclude it is safe (1)
                if (!knowledgeBase.isSatisfiable(new VecInt(new int[]{ i }))) {
                    results[i] = 1;
                // unsatisfiable with field i NOT a bomb, conclude it is a bomb (-1)
                } else if (!knowledgeBase.isSatisfiable(new VecInt(new int[]{ -i }))) {
                    results[i] = -1;
                }
                // otherwise we know nothing for sure, leave as 0
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return results;
    }

    private ArrayList<Integer> getNeighbours(int x, int y) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;
                if (x + dx < 0 || x + dx >= field.getNumOfCols())
                    continue;
                if (y + dy < 0 || y + dy >= field.getNumOfRows())
                    continue;
                list.add(getFieldFromCoordinate(x + dx, y + dy));
            }
        }
        return list;
    }

    
    // start indexing at 1 because 0 is invalid literal in SAT solver
    private int getFieldFromCoordinate(int x, int y) {
        return (x + 1) + y * field.getNumOfCols();
    }

    private int getXFromField(int f) {
        return (f-1) % field.getNumOfCols();
    }

    private int getYFromField(int f) {
        return (f-1) / field.getNumOfCols();
    }

    @Override
    public boolean solve() {
        MAXVAR = field.getNumOfCols() * field.getNumOfRows();
        board = new int[MAXVAR + 1];
        knowledgeBase.newVar(MAXVAR);
        rand = new Random();
        HashSet<Integer> safeFields = new HashSet<>();

        while(!this.field.solved()) {
            int feedback;
            int x;
            int y;

            if (safeFields.isEmpty()) { // no safe moves, guess
                System.out.println("LUCKY PLS");
                int i = rand.nextInt(MAXVAR) + 1;
                while(board[i] != 0) {
                    i = rand.nextInt(MAXVAR) + 1;
                }
                x = getXFromField(i);
                y = getYFromField(i);
            } else {
                int f = safeFields.iterator().next();
                safeFields.remove(f);
                x = getXFromField(f);
                y = getYFromField(f);
            }

            feedback = field.uncover(x, y);
            System.out.println("x: " + x + ", y: " + y);
            System.out.println(field.toString());

            if (feedback == -1) { // hit a bomb
                return false;
            } else {
                board[getFieldFromCoordinate(x,y)] = 1; // don't uncover this field again

                try {
                    addKnowledge(feedback, x, y); // add new constraints to KB
                } catch (ContradictionException e) {
                    System.err.println("Contradiction exception.");
                    return false;
                }

                // if no surrounding bombs, any covered fields are safe to uncover
                if (feedback == 0) {
                    board[getFieldFromCoordinate(x,y)] = 1;
                    for(int n : getNeighbours(x, y)) {
                        if(board[n] == 0) {
                            safeFields.add(n);
                            board[n] = 1;
                        }
                    }
                }

                // ask the KB for new information
                // deductions[i] == 1 means field i is safe, -1 -> bomb
                // see askKB()
                int[] deductions = askKB();
                for (int i = 1; i <= MAXVAR; i++) {
                    int deduction = deductions[i];
                    if (deduction == 0) {
                        continue;
                    }

                    if (deduction == 1 && board[i] == 0) {
                        safeFields.add(i);
                        board[i] = 1;
                    } else if (deduction == -1) {
                        board[i] = -1;
                    }
                }
            }
        }

    	return true;
    }

    public static void main(String[] args) {
        AgentOfLight agent = new AgentOfLight();
        agent.setField(new MSField("fields/fortgeschrittene1-16x16-40.txt"));        
        
        System.out.println(agent.solve());
    }

}
