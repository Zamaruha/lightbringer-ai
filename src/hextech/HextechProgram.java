package hextech;

import api.MSField;

public class HextechProgram {
    
    public static void main(String[] args) {
        int iterations = 10;
        int success = 0;
        int count = 0;
        
        for (int i = 0; i < iterations; i++) {
            HextechAgent agent = new HextechAgent(false);
            agent.setField(new MSField("fields/profi2-30x16-99.txt"));

            boolean solved = agent.solve();
            if (solved) {
                success++;
            }
            count++;
            double rate = (double) success / (double) count;
            System.out.println("Solved: " + success + ", Iterations: " + count
                    + ", Rate: " + rate);
        }
        double rate = (double) success / (double) iterations;
        System.out.println("Solved: " + success + ", Iterations: " + iterations
                + ", Rate: " + rate);
    }

}
