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

    private ArrayList<int[]> knowledgeBase = new ArrayList<int[]>();
    
    private void addKnowledge(int value, int x, int y) {
        int intVal = this.getFieldFromCoordinate(x, y);
        knowledgeBase.add(new int[]{ intVal * -1 });
        ArrayList<Integer> neighbours = getNeighbours(x, y);
        
        // Get the maximum
        int[] combos = Utils.listToArray(neighbours);
        populateKB(combos, value + 1, 0, new int[value + 1], -1);
        
        // Get the minimum
        int count = neighbours.size() - value + 1;
        populateKB(combos, count, 0, new int[count], 1);
        
        for (int i = 0; i < knowledgeBase.size(); i++) {
            System.out.println(Arrays.toString(knowledgeBase.get(i)));
        }
        
    }
    

  
    private void populateKB(int[] arr, int len, int startPosition, int[] result, int scalar){
        if (len == 0) {
            knowledgeBase.add(Utils.multiply(result, scalar));
            return;
        }       
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            populateKB(arr, len-1, i+1, result, scalar);
        }
    }
    
    private int[] askKB() {
        final int MAXVAR = field.getNumOfCols() * field.getNumOfRows();
        final int NBCLAUSES = knowledgeBase.size();

        ISolver solver = SolverFactory.newDefault();
        Reader reader = new DimacsReader(solver);

        solver.newVar(MAXVAR);
        solver.setExpectedNumberOfClauses(NBCLAUSES);

        for (int i = 0; i < NBCLAUSES; i++) {
            try {
                solver.addClause(new VecInt(knowledgeBase.get(i)));

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
                if (x + dx < 0 || x + dx >= field.getNumOfCols()) continue;
                if (y + dy < 0 || y + dy >= field.getNumOfRows()) continue;
                list.add(getFieldFromCoordinate(x + dx, y + dy));
            }
        }
        return list;
    }
    
    private int getFieldFromCoordinate(int x, int y) {
        return (x + 1) + y * field.getNumOfCols();
    }

    @Override
    public boolean solve() {
        field.uncover(0, 0);
        return field.solved();
    }
    
    public static void main(String[] args) {
        AgentOfLight agent = new AgentOfLight();
        agent.setField(new MSField("fields/baby-7x7-1.txt"));
        System.out.println(agent.field);
        int x = 0;
        int y = 2;
        int value = agent.field.uncover(x, y);
        System.out.println(agent.field);

        if (value == -1) {
            System.out.println("BOMB!");
            return;
        }
        System.out.println("Value: " + value);
        agent.addKnowledge(value, x, y);
        agent.askKB();
        //System.out.println(agent.getNeighbours(6, 1));
        
        
    }

}
