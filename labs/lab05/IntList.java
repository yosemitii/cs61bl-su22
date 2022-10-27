/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * @author Maurice Lee and Wan Fung Chui
 */

public class IntList {

    /** The integer stored by this node. */
    public int item;
    /** The next node in this IntList. */
    public IntList next;

    /** Constructs an IntList storing ITEM and next node NEXT. */
    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an IntList storing ITEM and no next node. */
    public IntList(int item) {
        this(item, null);
    }

    /** Returns an IntList consisting of the elements in ITEMS.
     * IntList L = IntList.list(1, 2, 3);
     * System.out.println(L.toString()) // Prints 1 2 3 */
    public static IntList of(int... items) {
        /** Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }
        /** Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;
        /** Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }
        return head;
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public int get(int position) {
        //TODO: YOUR CODE HERE
        IntList p = this;
        while ((p.next != null)&&(position != 0)){
            p = p.next;
            position -= 1;
        }
        if ((p.next == null)&&(position != 0)){
            throw new IllegalArgumentException("Index exceed");
        }
        else if ((p.next != null)&&(position == 0))
        {
            return p.item;
        }
        else{
            return p.item;
        }
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        //TODO: YOUR CODE HERE
        IntList p1 = this;
        if (p1 != null) {
            Integer item = p1.item;
            String str = item.toString();
            while (p1.next != null) {
                item = p1.next.item;
                str = str + " " + item.toString();
                p1 = p1.next;
            }
            return str;
        }
        else {
            return null;
        }
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * NOTE: A full implementation of equals requires checking if the
     * object passed in is of the correct type, as the parameter is of
     * type Object. This also requires we convert the Object to an
     * IntList, if that is legal. The operation we use to do this is called
     * casting, and it is done by specifying the desired type in
     * parenthesis. An example of this is on line 84.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IntList)) {
            return false;
        }
        //TODO: YOUR CODE HERE
        //? what is this (), to state the type again?
        IntList otherLst = (IntList) obj;
        IntList thisLst = this;
        while ((otherLst.next != null) && ((thisLst.next != null))&& (otherLst.item == thisLst.item)){
            otherLst = otherLst.next;
            thisLst = thisLst.next;
        }
        if ((otherLst.next == null) ^ (thisLst.next == null)){
            return false;
        }
        else if(otherLst.item == thisLst.item){
            return true;
        }
        else {return false;}
    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(int value) {
        //TODO: YOUR CODE HERE
        IntList tail = this;
        while (tail.next != null){
            tail = tail.next;
        }
        tail.next = new IntList(value);
    }

    /**
     * Returns the smallest element in the list.
     *
     * @return smallest element in the list
     */
    public int smallest() {
        //TODO: YOUR CODE HERE
        IntList p = this;
        int smallest = p.item;
        while (p.next != null){
            p = p.next;
            if (smallest > p.item){
                smallest = p.item;
            }
        }
        return smallest;
    }

    /**
     * Returns the sum of squares of all elements in the list.
     *
     * @return The sum of squares of all elements.
     */
    public int squaredSum() {
        //TODO: YOUR CODE HERE
        IntList p = this;
        int sqrt_sum = p.item * p.item;
        while (p.next != null){
            p = p.next;
            sqrt_sum += p.item * p.item;
        }

        return sqrt_sum;
    }

    /**
     * Destructively squares each item of the list.
     *
     * @param L list to destructively square.
     */
    public static void dSquareList(IntList L) {
        while (L != null) {
            L.item = L.item * L.item;
            L = L.next;
        }
    }

    /**
     * Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListIterative(IntList L) {
        if (L == null) {
            return null;
        }
        IntList res = new IntList(L.item * L.item, null);
        IntList ptr = res;
        L = L.next;
        while (L != null) {
            ptr.next = new IntList(L.item * L.item, null);
            L = L.next;
            ptr = ptr.next;
        }
        return res;
    }

    /** Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        return new IntList(L.item * L.item, squareListRecursive(L.next));
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
    public static IntList dcatenate(IntList A, IntList B) {
        //TODO: YOUR CODE HERE
        if ((A == null)&& (B == null)){return null;}
        else if ((A == null) && (B != null)){return B;}
        else if ((A != null) && (B == null)){return A;}
        IntList p = A;
        while(p.next != null){
            p = p.next;
        }
        p.next = B;
        return A;

    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * non-destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
     public static IntList catenate(IntList A, IntList B) {
         //TODO: YOUR CODE HERE
         if ((A == null)&& (B == null)){return null;}
         else if ((A == null) && (B != null)){return B;}
         else if ((A != null) && (B == null)){return A;}

         IntList res = new IntList(A.item, null);
         IntList ptr = res;
         A = A.next;
         while(A != null){
             ptr.next = new IntList(A.item);
             ptr = ptr.next;
             A = A.next;
         }
         while(B != null){
             ptr.next = new IntList(B.item);
             ptr = ptr.next;
             B = B.next;
         }
         return res;
     }
}