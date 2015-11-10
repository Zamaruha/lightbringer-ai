package api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.sat4j.core.*;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

/**
* This agent uncovering positions randomly.
* It obviously does not use a SAT solver.
* 
*/
public class RandomMSAgent extends MSAgent {
	
	private Random rand = null;
	
	@Override
	public boolean solve() {
		
		final int MAXVAR = 1000000;
		final int NBCLAUSES = 500000;

		ISolver solver = SolverFactory.newDefault();

		// prepare the solver to accept MAXVAR variables. MANDATORY for MAXSAT solving
		solver.newVar(MAXVAR);
		solver.setExpectedNumberOfClauses(NBCLAUSES);
		// Feed the solver using Dimacs format, using arrays of int
		// (best option to avoid dependencies on SAT4J IVecInt)
		for (int i=0; i < NBCLAUSES; i++) {
		  int [] clause = { 1, -3, 4, 1 };
		  // while int [] clause = {1, -3, 7, 0}; is not fine 
		  try {
			solver.addClause(new VecInt(clause));
		  } catch (ContradictionException e) {
			e.printStackTrace();
		  }
		}

		// we are done. Working now on the IProblem interface
		IProblem problem = solver;
		try {
			if (problem.isSatisfiable()) {
			   System.out.println("Satisfiable!");
			} else {
				System.out.println("Not satisfiable!");
			}
		} catch (TimeoutException e) {
			e.printStackTrace();
		}
		
		
		
		this.rand = new Random();
		int numOfRows = this.field.getNumOfRows();
		int numOfCols = this.field.getNumOfCols();
		int x, y, feedback;
		do {
			x = rand.nextInt(numOfCols);
			y = rand.nextInt(numOfRows);
			feedback = field.uncover(x,y);
			System.out.println(this.field);
			
		} while(feedback >= 0 && !field.solved());
		
		return field.solved();
	}

}
