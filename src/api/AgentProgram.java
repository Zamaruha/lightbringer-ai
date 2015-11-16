package api;

import java.util.Arrays;
import java.util.List;

public class AgentProgram {
    
    private static String[] fields = new String[] {
            "fields/profi5-30x16-99.txt", // 0
            "fields/anfaenger1-9x9-10.txt", // 1
            "fields/anfaenger2-9x9-10.txt", // 2
            "fields/anfaenger3-9x9-10.txt", // 3
            "fields/anfaenger4-9x9-10.txt", // 4
            "fields/anfaenger5-9x9-10.txt", // 5
            "fields/baby-3x3-0.txt", // 6
            "fields/baby-3x3-1.txt", // 7
            "fields/baby-5x5-1.txt", // 8
            "fields/baby-5x5-3.txt", // 9
            "fields/baby-5x5-5.txt", // 10
            "fields/baby-7x7-1.txt", // 11
            "fields/baby-7x7-10.txt", // 12
            "fields/baby-7x7-3.txt", // 13
            "fields/baby-7x7-5.txt", // 14
            "fields/fortgeschrittene1-16x16-40.txt", // 15
            "fields/fortgeschrittene2-16x16-40.txt", // 16
            "fields/fortgeschrittene3-16x16-40.txt", // 17
            "fields/fortgeschrittene4-16x16-40.txt", // 18
            "fields/fortgeschrittene5-16x16-40.txt", // 19
            "fields/profi1-30x16-99.txt", // 20
            "fields/profi2-30x16-99.txt", // 21
            "fields/profi3-30x16-99.txt", // 22
            "fields/profi4-30x16-99.txt" // 23
    };
    
    public static void main(String[] args) {
        int iterations = 20;
        int success = 0;
        int count = 0;
        long totalRunTime = 0;
        
        for (int i = 0; i < iterations; i++) {
            long startTime = System.nanoTime();
            AgentOfLight agent = new AgentOfLight();
            agent.setField(new MSField(fields[21]));

            boolean solved = agent.solve();
            long runTime = (System.nanoTime() - startTime) / 1000000;
            
            if (solved) {
                success++;
                totalRunTime += runTime;
            }
            count++;
            double rate = (double) success / (double) count;
            
            
            System.out.println("Solved: " + solved + ", Iteration: " + count
                    + ", Run Time (ms): " + runTime);
            
        }
        double rate = (double) success / (double) iterations;
        double avgRT = (double) totalRunTime / (double) success;
        System.out.println("Solved: " + success + " of " + iterations
                + ", Rate: " + rate + ", Average RT (ms): " + avgRT);
    }

}
