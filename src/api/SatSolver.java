package api;

import java.util.ArrayList;
import java.util.Arrays;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;
import org.sat4j.tools.ModelIterator;


public class SatSolver {
    
    private ISolver solver;
    
    public SatSolver() {
        solver = new ModelIterator(SolverFactory.newDefault());
    }

    public ArrayList<int[]> askKnowledgeBase(ArrayList<int[]> knowledgeBase, int maxVar) {
        final int NBCLAUSES = knowledgeBase.size();
        ArrayList<int[]> models = new ArrayList<int[]>();
        solver.clearLearntClauses();
        solver.newVar(maxVar);
        solver.setExpectedNumberOfClauses(NBCLAUSES);

        for (int i = 0; i < NBCLAUSES; i++) {
            try {
                solver.addClause(new VecInt(knowledgeBase.get(i)));

            } catch (ContradictionException e) {
                System.out.println("CONTRADICTION");
                return models;
            }
        }

        IProblem problem = solver;
        try {
            while (problem.isSatisfiable()) {
                int[] model = problem.model();
                models.add(model);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return models;
    }
}
