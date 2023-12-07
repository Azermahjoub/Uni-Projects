import java.util.LinkedList;

/**
 * This class implements a game of Row of Bowls.
 * For the games rules see Blatt05. The goal is to find an optimal strategy.
 */
public class RowOfBowls {
    public int[][] results;
    public int [] values;
    public RowOfBowls() {
    }
    
    /**
     * Implements an optimal game using dynamic programming
     * @param values array of the number of marbles in each bowl
     * @return number of game points that the first player gets, provided both parties play optimally
     */
    public int maxGain(int[] values)
    {
        // TODO
        this.values = values;
        this.results = new int[values.length][values.length];
        int i = 0;
        int j = 0;
        while (i < values.length) {
            results[i][j] = values[i];
            i++;
            j++;
        }
        for (int eq = 1; eq <= values.length-1; eq++) {
            j = eq;
            i = 0;
            while (j < values.length){
                results[i][j] = opt(results,i,j,eq);
                j++;
                i++;
            }
        }
        return results[0][values.length-1];
    }
    public int opt(int [][] results, int i,int j, int eq){
        return  Integer.max(results[i][j-eq]-results[i+1][j],results[i+eq][j]-results[i][j-1]);
    }

    /**
     * Implements an optimal game recursively.
     *
     * @param values array of the number of marbles in each bowl
     * @return number of game points that the first player gets, provided both parties play optimally
     */
    public int maxGainRecursive(int[] values) {
        // TODO
        this.values = values;
        int anfang = 0;
        int ende = values.length - 1;
        return maxGainRecursive(anfang, ende, values);
    }
    public int maxGainRecursive(int anfang, int ende, int[] values) {
        if (anfang == ende - 1) {
            return Integer.max(values[anfang] - values[ende], values[ende] - values[anfang]);
        } else {
            return Integer.max(values[anfang] - maxGainRecursive(anfang + 1, ende, values),
                    values[ende] - maxGainRecursive(anfang, ende - 1, values));
        }
    }

    
    /**
     * Calculates an optimal sequence of bowls using the partial solutions found in maxGain(int values)
     * @return optimal sequence of chosen bowls (represented by the index in the values array)
     */
    public Iterable<Integer> optimalSequence()
    {
        // TODO
        LinkedList<Integer> seq = new LinkedList<>();
        int i = 0;
        int j = values.length-1;
        int eq = values.length-1;
        while (seq.size()<values.length){
            if (i==j){
                seq.add(i);
            }
            else if (results[i][j] == (results[i][j-eq]-results[i+1][j])){
                seq.add(i);
                i++;
                eq--;
            }
            else {
                seq.add(j);
                j--;
                eq--;
            }
        }
        return seq;
    }


    public static void main(String[] args) {
        // For Testing
    }
}

