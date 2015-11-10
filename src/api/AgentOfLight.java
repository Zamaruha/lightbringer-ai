package api;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class AgentOfLight extends MSAgent {

    private Random rand;
    private ArrayList<int[]> KB = new ArrayList<int[]>();
    
    private void addKnowledge(int value, int x, int y) {
        int intVal = this.getFieldFromCoordinate(x, y);
        KB.add(new int[]{ intVal * -1 });
        ArrayList<Integer> neighbours = getNeighbours(x, y);
        
        // Get the maximum
        int[] combos = this.convertIntegers(neighbours);
        populateKB(combos, value + 1, 0, new int[value + 1]);
        
        // Get the minimum
        int count = neighbours.size() - value + 1;
        populateKB(combos, count, 0, new int[count]);
        
        for (int i = 0; i < KB.size(); i++) {
            System.out.println(Arrays.toString(KB.get(i)));
        }
        
    }
  
    private void populateKB(int[] arr, int len, int startPosition, int[] result){
        if (len == 0) {
            int[] clause = new int[result.length];
            System.arraycopy(result, 0, clause, 0, result.length);
            // Invert all the numbers
            for (int i = 0; i < clause.length; i++) {
                clause[i] = clause[i] * -1;
            }
            KB.add(clause);
            return;
        }       
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            populateKB(arr, len-1, i+1, result);
        }
    }
    
    private int[] askKB() {
        final int MAXVAR = field.getNumOfCols() * field.getNumOfRows();
        final int NBCLAUSES = KB.size();

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);

        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);

        for (int i = 0; i < NBCLAUSES; i++) {
            try {
                solver.addClause(new VecInt(KB.get(i)));

            } catch (ContradictionException e) {
                e.printStackTrace();
            }
        }

        IProblem problem = solver;
        try {
            if (problem.isSatisfiable()) {
                System.out.println("Satisfiable!");
                System.out.println(reader.decode(problem.model()));
                return problem.model();
            } else {
                System.out.println("Not satisfiable!");
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private ArrayList<Integer> getNeighbours(int x, int y) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (x + dx < 0 || x + dx > field.getNumOfCols()) continue;
                if (y + dy < 0 || y + dy > field.getNumOfRows()) continue;
                list.add(getFieldFromCoordinate(x + dx, y + dy));
            }
        }
        return list;
    }
    
    private int getFieldFromCoordinate(int x, int y) {
        return x + y * field.getNumOfCols();
    }


    @Override
    public boolean solve() {
        
        this.rand = new Random();
        int numOfRows = this.field.getNumOfRows();
        int numOfCols = this.field.getNumOfCols();
        int x, y, feedback;
        do {
            x = rand.nextInt(numOfCols);
            y = rand.nextInt(numOfRows);
            feedback = field.uncover(x, y);
            System.out.println("x: " + x + ", y: " + y);
            System.out.println(this.field);

        } while (feedback >= 0 && !field.solved());
        
        return field.solved();
    }
    
    private int[] convertIntegers(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
    
    public static void main(String[] args) {
        AgentOfLight agent = new AgentOfLight();
        agent.setField(new MSField("fields/baby-7x7-1.txt"));
        System.out.println(agent.field);
        int x = 0;
        int y = 4;
        int value = agent.field.uncover(x, y);
        System.out.println("Value: " + value);
        agent.addKnowledge(value, x, y);
        agent.askKB();
    }

}
