package de.unima.info.ki.minesweeper.api;

public class UsageExample {

	public static void main(String[] args) {

		// use smaller numbers for larger fields 
		int iterations = 10000;
		
		int success = 0;
		for (int i = 0; i < iterations; i++) {
			i++;
			MSField f = new MSField("fields/baby-7x7-1.txt");
			RandomMSAgent agent = new RandomMSAgent();
			agent.setField(f);
			
			boolean solved = agent.solve();
			if (solved) {
				success++;	
			}
		}
		double rate = (double) success / (double) iterations;
		System.out.println("Solved: " + success+  ", Iterations: " + iterations + ", Rate: " + rate);
		
	}

}
