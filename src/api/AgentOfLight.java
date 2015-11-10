package api;

import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;

public class AgentOfLight extends MSAgent {

    private Random rand;
    
    private void printKnowledge(int value, int x, int y) {
        int intVal = this.getFieldFromCoordinate(x, y);
        ArrayList<ArrayList<Integer>> clauses = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> neighbours = getNeighbours(x, y);
        System.out.println(neighbours.toString());
        
        int[] combos = new int[neighbours.size()];
        for (int i = 0; i < neighbours.size(); i++) {
            combos[i] = neighbours.get(i);
        }
        
        combo(combos, value + 1, 0, new int[value + 1]);
        
        
        /*int[] combos = new int[value];
        for (int i = 0; i < value; i++) {
            combos[i] = i;
        }
        
        int index = value - 1;
        while (true) {
            ArrayList<Integer> innerList = new ArrayList<Integer>();
            for (int m = 0; m < value; m++) {
                innerList.add(neighbours.get(combos[m]));
            }
            combos[index]++;
            while (combos[index] >= neighbours.size() - (value - (index + 1))) {
                index--;
                if (index < 0) {
                    break;
                }
            }
            
            if (index < 0) {
                break;
            }
        }*/
        
    }
    

    // combinations2(arr, 3, 0, new String[3]);

    private void combo(int[] arr, int len, int startPosition, int[] result){
        if (len == 0) {
            System.out.println(Arrays.toString(result));
            return;
        }       
        for (int i = startPosition; i <= arr.length-len; i++){
            result[result.length - len] = arr[i];
            combo(arr, len-1, i+1, result);
        }
    }  
    
    private ArrayList<Integer> getNeighbours(int x, int y) {
        ArrayList<Integer> list = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                if (x + dx < 0 || x + dx > field.getNumOfCols()) continue;
                if (y + dy < 0 || y + dy > field.getNumOfRows()) continue;
                list.add(getFieldFromCoordinate(x + dx, y + dy));
            }
        }
        return list;
    }
    
    private int getFieldFromCoordinate(int x, int y) {
        return x + y * field.getNumOfCols();
    }


    @Override
    public boolean solve() {
        
        this.rand = new Random();
        int numOfRows = this.field.getNumOfRows();
        int numOfCols = this.field.getNumOfCols();
        int x, y, feedback;
        do {
            x = rand.nextInt(numOfCols);
            y = rand.nextInt(numOfRows);
            feedback = field.uncover(x, y);
            System.out.println("x: " + x + ", y: " + y);
            System.out.println(this.field);

        } while (feedback >= 0 && !field.solved());
        
        return field.solved();
    }
    
    public static void main(String[] args) {
        AgentOfLight agent = new AgentOfLight();
        agent.setField(new MSField("fields/baby-7x7-1.txt"));
        agent.printKnowledge(2, 0, 3);
    }

}
