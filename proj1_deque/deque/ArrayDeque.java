package deque;

public class ArrayDeque<T> implements Deque<T> {
    private T[] elems;
    private int size;
    private int nextFirst;
    private int nextLast;
    private int maxLength;

    public ArrayDeque(){
        maxLength = 16;
        elems = (T[])new Object[maxLength];
        size = 0;
        nextFirst = 7;
        nextLast = 8;

    }

//    public ArrayDeque(T item){
//        maxLength = 16;
//        elems = (T[])new Object[16];
//        elems[8] = item;
//        size = 1;
//        nextFirst = 7;
//        nextLast = 9;
//    }
    private int leftMove(int index){
        if (index > 0){
            index -= 1;
        }
        else index = maxLength -1;
        return index;
    }

    private int rightMove(int index){
        if (index < maxLength - 1){
            index += 1;
        }
        else {
            index = 0;
        }
        return index;
    }


    public boolean equals(Object o){
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        ArrayDeque dq = (ArrayDeque) o;
//        if (size != dq.size()) return false;
//        int index = 0;
//        while (index < size){
//            index += 1;
//            if (get(index) != dq.get(index)) return false;
//        }
        if(!(o instanceof Deque)) return false;
        Deque dq = (Deque) o;
        if ((this == null) ^ (dq== null)) return false;
        if (this.size() != dq.size()) return false;
        int index = 0;
        while(index < size){
            if (!get(index).equals(dq.get(index))) return false;
            index += 1;
        }
        return true;
    }
    private T[] upResize(){
        T[] resized = (T[])new Object[2*maxLength];
        //when the old array is almost full, expand it 2 times, keep the elements, and re-assign the nextFirst and nextLast
        //only do this when nextFirst = 0, nextLast = maxLength = 1, which is case 1 (if)
        //or nextFirst = nextLast + 1, which is case 2 (else)
//        if (nextFirst < nextLast) {
//            for (int i = 0; i < maxLength; i++) {
//                resized[maxLength/2 + i] = elems[nextFirst + i];
//            }
//        }
//        else if (nextFirst == maxLength - 1){
//            for (int i = 0; i < size; i++){
//                resized[maxLength/2 +i] = elems[i];
//            }
//        }
//        else if (nextLast == 0){
//            for (int i = 0; i < size; i++){
//                resized[maxLength/2 + i] = elems[nextFirst + 1 +i];
//            }
//        }
//        else{
//            for (int i = 0; i < (maxLength - nextFirst -1); i++){
//                resized[maxLength/2 + i] = elems[nextFirst + 1 +i];
//            }
//            for (int j = 0; j < nextLast -1; j++){
//                resized[maxLength/2 +(maxLength - nextFirst -1) + j] = elems[j];
//            }
//        }
        int ptr = rightMove(nextFirst);
        int i = 0;
        while (ptr != nextLast){
        //when nextLast exceed and get a small value, it does not go into this loop
//        for (int i = 0; i < size; i++){
            resized[maxLength /2 + i] = elems[ptr];
            i += 1;
            ptr = rightMove(ptr);
        }
        nextFirst = maxLength/2 -1;
        nextLast = nextFirst + size + 1;
        maxLength = maxLength * 2;
        return resized;
    }

    //call this when the capacity is lower than 25%
    private T[] downResize(){
        T[] resized = (T[])new Object[maxLength/2];
//        if (nextFirst < nextLast) {
//            for (int i = 0; i < maxLength; i++) {
//                resized[maxLength/4 + i] = elems[nextFirst + i];
//            }
//        }
//        else if (nextFirst == maxLength - 1){
//            for (int i = 0; i < size; i++){
//                resized[maxLength/4 +i] = elems[i];
//            }
//        }
//        else if (nextLast == 0){
//            for (int i = 0; i < size; i++){
//                resized[maxLength/4 + i] = elems[nextFirst + 1 +i];
//            }
//        }
//        else {
//            for (int i = 0; i < (maxLength - nextFirst - 1); i++) {
//                resized[maxLength/2 + i] = elems[nextFirst + 1 + i];
//            }
//            for (int j = 0; j < nextLast - 1; j++) {
//                resized[maxLength/2 + (maxLength - nextFirst - 1) + j] = elems[j];
//            }
//        }
        int ptr = rightMove(nextFirst);
        int i = 0;
        //while loop need to be conditional here
        //for (int i = 0; i < size; i++){
        while (ptr != nextLast){
            resized[maxLength/8 +i] = elems[ptr];
            ptr = rightMove(ptr);
            i += 1;
        }
        nextFirst = maxLength/8 -1;
        nextLast = rightMove(nextFirst + size);
        maxLength = maxLength/2;
        return resized;
    }
    public void addFirst(T item){
        //if it is almost full
        if (nextFirst == rightMove(nextLast)){
            elems = upResize();
        }
        elems[nextFirst] = item;
        nextFirst = leftMove(nextFirst);
        size += 1;
    }
    public void addLast(T item){
        //if it is less than 25% capacity
        //if it is almost full
        if ((nextFirst == nextLast + 1) || ((nextFirst == 0) && (nextLast == maxLength-1))){
            elems = upResize();
        }
        elems[nextLast] = item;
        nextLast = rightMove(nextLast);
        size += 1;
    }

    public int size(){
//        if (isEmpty()) return 0;
//        int res = 0;
//        int ptr = nextFirst + 1;
//        while (ptr != nextLast){
//            res += 1;
//            ptr = rightMove(ptr);
//        }
//        return res;
        return size;
    }
    public T removeFirst(){
        if (isEmpty()) return null;
        if ((size <= maxLength/4) && (maxLength > 16)){
            elems = downResize();
        }
        //how to be more generic
        T removed = elems[rightMove(nextFirst)];
        elems[rightMove(nextFirst)] = null;
        nextFirst = rightMove(nextFirst);
        size -= 1;
        return removed;
    }
    public T removeLast(){
        if (isEmpty()) return null;
        if ((size <= maxLength/4) && (maxLength > 16)){
            elems = downResize();
        }
        T removed = elems[leftMove(nextLast)];
        elems[leftMove(nextLast)] = null;
        nextLast = leftMove(nextLast);
        size -= 1;
        return removed;
    }
    public T get(int index){
        int i = 0;
        int reIndex = rightMove(nextFirst);
        while (i < index){
            reIndex = rightMove(reIndex);
            i += 1;
        }
        return elems[reIndex];
    }

    public void printDeque(){
        if (isEmpty()) return;
        int index = nextFirst + 1;
        while (index < nextLast){
            System.out.print(elems[index] + " ");
            index = rightMove(index);
        }
        System.out.println();
    }

}
