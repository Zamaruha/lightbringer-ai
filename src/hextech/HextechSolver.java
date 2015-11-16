package hextech;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class HextechSolver {
    
    private ISolver solver;
    
    public HextechSolver() {
        solver = SolverFactory.newLight();
    }

    public Boolean ask(KnowledgeBase base, int maxVar) {
        final int NBCLAUSES = base.size();
        solver.reset();
        solver.newVar(maxVar);
        solver.setExpectedNumberOfClauses(NBCLAUSES);
        for (int i = NBCLAUSES - 1; i >= 0; i--) {
            try {
                solver.addClause(base.getVecInt(i));
            } catch (ContradictionException e) {
                //System.out.println(e.getMessage());
                //System.out.println("CONTRADICTION");
                return null;
            }
        }

        IProblem problem = solver;
        try {
            if(problem.isSatisfiable()) {
                //System.out.println("Satisfiable");
                return true;
            } else {
                //System.out.println("Not satisfiable");
                return false;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }
}
