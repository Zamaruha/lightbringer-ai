package de.unima.info.ki.minesweeper.api;

import java.util.Random;

/**
* This agent uncovering positions randomly.
* It obviously does not use a SAT solver.
* 
*/
public class RandomMSAgent extends MSAgent {

	private Random rand = null;
	
	@Override
	public boolean solve() {
		this.rand = new Random();
		int numOfRows = this.field.getNumOfRows();
		int numOfCols = this.field.getNumOfCols();
		int x, y, feedback;
		do {
			x = rand.nextInt(numOfCols);
			y = rand.nextInt(numOfRows);
			feedback = field.uncover(x,y);
			
		} while(feedback >= 0 && !field.solved());
		
		return field.solved();
	}

}
