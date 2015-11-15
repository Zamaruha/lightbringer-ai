package api;

import java.util.Arrays;
import java.util.List;

public class AgentProgram {
   
    private static String[] fields = new String[] {
            "fields/anfaenger1-9x9-10.txt",
            "fields/anfaenger2-9x9-10.txt",
            "fields/anfaenger3-9x9-10.txt",
            "fields/anfaenger4-9x9-10.txt",
            "fields/anfaenger5-9x9-10.txt",
            "fields/baby-3x3-0.txt",
            "fields/baby-3x3-1.txt",
            "fields/baby-5x5-1.txt",
            "fields/baby-5x5-3.txt",
            "fields/baby-5x5-5.txt",
            "fields/baby-7x7-1.txt",
            "fields/baby-7x7-10.txt",
            "fields/baby-7x7-3.txt",
            "fields/baby-7x7-5.txt",
            "fields/fortgeschrittene1-16x16-40.txt",
            "fields/fortgeschrittene2-16x16-40.txt",
            "fields/fortgeschrittene3-16x16-40.txt",
            "fields/fortgeschrittene4-16x16-40.txt",
            "fields/fortgeschrittene5-16x16-40.txt",
            "fields/profi1-30x16-99.txt",
            "fields/profi2-30x16-99.txt",
            "fields/profi3-30x16-99.txt",
            "fields/profi4-30x16-99.txt",
            "fields/profi5-30x16-99.txt",
    };
    
    public static void main(String[] args) {
        List<String> fieldList = Arrays.asList(fields);
        final int iterations = 20;
        
        for (int j = 0; j < fieldList.size(); j++) {
            int success = 0;
            for (int i = 0; i < iterations; i++) {
                MSAgent agent = new AgentOfLight();
                agent.setField(new MSField(fieldList.get(j)));

                boolean solved = agent.solve();
                if (solved) {
                    success++;
                }
            }
            double rate = (double) success / (double) iterations;
            System.out.println("Field: " + fieldList.get(j) + " (" + j + "/" + fieldList.size() + "), Rate: " + rate);
        }
    }

}
