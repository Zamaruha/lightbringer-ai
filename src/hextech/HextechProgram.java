package hextech;

import api.MSField;

public class HextechProgram {
    
    public static void main(String[] args) {
        int iterations = 1;
        int success = 0;
        
        for (int i = 0; i < iterations; i++) {
            HextechAgent agent = new HextechAgent(true);
            agent.setField(new MSField("fields/fortgeschrittene1-16x16-40.txt"));

            boolean solved = agent.solve();
            if (solved) {
                success++;
            }
        }
        double rate = (double) success / (double) iterations;
        System.out.println("Solved: " + success + ", Iterations: " + iterations
                + ", Rate: " + rate);
    }

}
