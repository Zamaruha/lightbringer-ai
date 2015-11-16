package hextech;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

public class HextechSolver {
    
    private ISolver solver;
    
    public HextechSolver() {
        solver = SolverFactory.newLight();
    }

    public Boolean ask(KnowledgeBase base, int maxVar) {
        solver.reset();
        solver.newVar(maxVar);
        solver.setExpectedNumberOfClauses(base.size());
        for (int i = base.size() - 1; i >= 0; i--) {
            try {
                solver.addClause(base.getVecInt(i));
            } catch (ContradictionException e) {
                return null;
            }
        }

        try {
            return solver.isSatisfiable();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }
}
