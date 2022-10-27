import java.util.ArrayList;
import java.util.List;

/**
 * Represent a set of ints.
 */
public class ListSet implements SimpleSet {

    List<Integer> elems;

    public ListSet() {
        elems = new ArrayList<Integer>();
    }

    /** Adds k to the set. */
    public void add(int k) {
        // TODO
        int listSize = elems.size();
        for (int j=0; j < listSize; j++){
            if (elems.get(j) == k){
                return;
            }
        }
        elems.add(k);
    }

    /** Removes k from the set. */
    public void remove(int k) {
        Integer toRemove = k;
        // TODO - use the above variable with an appropriate List method.
        // The reason is beyond the scope of this lab, but involves
        // method resolution.
        int listSize = elems.size();
        for (int j=0; j < listSize; j++){
            if (elems.get(j) == toRemove){
                elems.remove(toRemove);
                return;
            }
        }
        return;
    }

    /** Return true if k is in this set, false otherwise. */
    public boolean contains(int k) {
        // TODO
        int listSize = elems.size();
        for (int j=0; j < listSize; j++){
            if (elems.get(j) == k){
                return true;
            }
        }
        return false;
    }

    /** Return true if this set is empty, false otherwise. */
    public boolean isEmpty() {
        return elems.size() ==0;
    }

    /** Returns the number of items in the set. */
    public int size() {
        // TODO
        return elems.size();
    }

    /** Returns an array containing all of the elements in this collection. */
    public int[] toIntArray() {
        // TODO - use a for loop!
        int[] res = new int[size()];
        if (isEmpty() == false) {
            for (int i = 0; i < size(); i++) {
                res[i] = elems.get(i);
            }
        }
        return res;
    }
}
