package hextech;

import api.MSField;

public class HextechProgram {
    
    private static String[] fields = new String[] {
            "fields/profi5-30x16-99.txt",            //  0 (42/100)
            "fields/anfaenger1-9x9-10.txt",          //  1 (57/100)
            "fields/anfaenger2-9x9-10.txt",          //  2 (50/100)
            "fields/anfaenger3-9x9-10.txt",          //  3 (71/100)
            "fields/anfaenger4-9x9-10.txt",          //  4 (65/100)
            "fields/anfaenger5-9x9-10.txt",          //  5 (72/100)
            "fields/baby-3x3-0.txt",                 //  6 (100/100)
            "fields/baby-3x3-1.txt",                 //  7 (66/100)
            "fields/baby-5x5-1.txt",                 //  8 (99/100)
            "fields/baby-5x5-3.txt",                 //  9 (87/100)
            "fields/baby-5x5-5.txt",                 // 10 (72/100)
            "fields/baby-7x7-1.txt",                 // 11 (94/100)
            "fields/baby-7x7-10.txt",                // 12 (30/100)
            "fields/baby-7x7-3.txt",                 // 13 (90/100)
            "fields/baby-7x7-5.txt",                 // 14 (87/100)
            "fields/fortgeschrittene1-16x16-40.txt", // 15 (65/100)
            "fields/fortgeschrittene2-16x16-40.txt", // 16 (60/100)
            "fields/fortgeschrittene3-16x16-40.txt", // 17 (31/100)
            "fields/fortgeschrittene4-16x16-40.txt", // 18 (45/100)
            "fields/fortgeschrittene5-16x16-40.txt", // 19 (10/100)
            "fields/profi1-30x16-99.txt",            // 20
            "fields/profi2-30x16-99.txt",            // 21
            "fields/profi3-30x16-99.txt",            // 22
            "fields/profi4-30x16-99.txt"             // 23
    };
    
    public static void main(String[] args) {
        int iterations = 20;
        solveField(iterations, fields[0]);
    }
    
    private static void solveField(int iterations, String fieldName) {
        int success = 0;
        int count = 0;
        long totalRunTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            HextechAgent agent = new HextechAgent(false);
            agent.setField(new MSField(fieldName));

            boolean solved = agent.solve();
            long runTime = (System.nanoTime() - startTime) / 1000000;
            
            if (solved) {
                success++;
                totalRunTime += runTime;
            }
            count++;
            //double rate = (double) success / (double) count;
            //System.out.println("Solved: " + solved + ", Iteration: " + count
            //        + ", Run Time (ms): " + runTime);
            
        }
        double rate = (double) success / (double) iterations;
        double avgRT = (double) totalRunTime / (double) success;
        System.out.println("Results for: " + fieldName);
        System.out.println("Solved: " + success + " of " + iterations
                + ", Rate: " + rate + ", Average RT (ms): " + avgRT + "\n");
    }

}
