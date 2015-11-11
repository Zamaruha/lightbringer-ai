package api;

import java.util.Iterator;
import java.util.List;

public class Utils {

    public static int[] multiply(int[] arr, int scalar) {
        int[] newArr = new int[arr.length];
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        for (int i = 0; i < newArr.length; i++) {
            newArr[i] = newArr[i] * scalar;
        }
        return newArr;
    }
    
    public static int[] listToArray(List<Integer> integers)
    {
        int[] ret = new int[integers.size()];
        Iterator<Integer> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }
    
}
