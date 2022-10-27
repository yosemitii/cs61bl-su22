public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        for (int i = pos; i < values.length-1; i++){
            values[i] = values[i+1];
        }
        values[values.length-1] = 0;
        return;
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;

        }
        // TODO: YOUR CODE HERE
        for (int i = values.length -1; i > pos; i--){
            values[i] = values[i-1];
        }
        values[pos] = newInt;
        return;
    }

    /** 
     * Returns a new array consisting of the elements of A followed by the
     *  the elements of B. 
     */
    public static int[] catenate(int[] A, int[] B) {
        // TODO: YOUR CODE HERE
        int totalLen = A.length + B.length;
        int[] C;
        C = new int[totalLen];
        for (int i = 0; i < totalLen; i++){
            if (i < A.length){
                C[i] = A[i];
            }
            else {
                C[i] = B[i-A.length];
            }
        }
        return C;
    }

}