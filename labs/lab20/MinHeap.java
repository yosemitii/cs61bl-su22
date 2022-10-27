import java.util.ArrayList;
import java.util.NoSuchElementException;

/* A MinHeap class of Comparable elements backed by an ArrayList. */
public class MinHeap<E extends Comparable<E>> {

    /* An ArrayList that stores the elements in this MinHeap. */
    private ArrayList<E> contents;
    private int size;
    // TODO: YOUR CODE HERE (no code should be needed here if not 
    // implementing the more optimized version)

    /* Initializes an empty MinHeap. */
    public MinHeap() {
        contents = new ArrayList<>();
        contents.add(null);
        size = 0;
    }

    /* Returns the element at index INDEX, and null if it is out of bounds. */
    private E getElement(int index) {
        if (index >= contents.size() || index == -1) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    /* Sets the element at index INDEX to ELEMENT. If the ArrayList is not big
       enough, add elements until it is the right size. */
    private void setElement(int index, E element) {
        while (index >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, element);
    }

    /* Swaps the elements at the two indices. */
    private void swap(int index1, int index2) {
        E element1 = getElement(index1);
        E element2 = getElement(index2);
        setElement(index2, element1);
        setElement(index1, element2);
    }

    /* Prints out the underlying heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getElement(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getElement(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getElement(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getElement(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* Returns the index of the left child of the element at index INDEX. */
    private int getLeftOf(int index) {
        int left = 2 * index;
        if (left <= size){
            return left;
        }
        else {
            return -1;
        }
    }

    /* Returns the index of the right child of the element at index INDEX. */
    private int getRightOf(int index) {
        // TODO: YOUR CODE HERE
        int right = 2 * index + 1;
        if (right <= size){
            return right;
        }
        else {
            return -1;
        }
    }

    /* Returns the index of the parent of the element at index INDEX. */
    private int getParentOf(int index) {
        // TODO: YOUR CODE HERE
        if (index == 1) {
            return 0;
        }
        else if (index % 2 == 0){
            return index / 2;
        }
        else {
            return (index - 1) / 2;
        }
    }

    /* Returns the index of the smaller element. At least one index has a
       non-null element. If the elements are equal, return either index. */
    private int min(int index1, int index2) {
        // TODO: YOUR CODE HERE
        E content1 = contents.get(index1);
        E content2 = contents.get(index2);
        if (content1.compareTo(content2) > 0) {
            return index2;
        }
        else {
            return index1;
        }
    }

    /* Returns but does not remove the smallest element in the MinHeap. */
    public E findMin() {
        // TODO: YOUR CODE HERE
        return contents.get(1);
    }

    /* Bubbles up the element currently at index INDEX. */
    private void bubbleUp(int index) {
        // TODO: YOUR CODE HERE
        while (getParentOf(index) != 0) {
            int parentIndex = getParentOf(index);
            E parent = contents.get(parentIndex);
            E current = contents.get(index);
            if (current.compareTo(parent) < 0) {
                swap(index, parentIndex);
                bubbleUp(parentIndex);
            }
            return;
        }
    }

    /* Bubbles down the element currently at index INDEX. */
    private void bubbleDown(int index) {
        // TODO: YOUR CODE HERE
        if (getLeftOf(index) != -1 || getRightOf(index) != -1) {
//            System.out.println("index now:" + index);
            int leftChildIndex = getLeftOf(index);
            int rightChildIndex = getRightOf(index);
            E current = contents.get(index);
            E leftChild = null;
            E rightChild = null;
            if (leftChildIndex != -1) {
                leftChild = contents.get(leftChildIndex);
            }
            if (rightChildIndex != -1) {
                rightChild = contents.get(rightChildIndex);
            }
            if (rightChild == null) {
                if (leftChild.compareTo(current) < 0) {
                    swap(index, leftChildIndex);
                    index = leftChildIndex;
                }
                else {return;}
            }
            else if (leftChild.compareTo(current) < 0 || rightChild.compareTo(current) < 0) {
                int minIndex = min(leftChildIndex, rightChildIndex);
                swap(index, minIndex);
                index = minIndex;
            }
        }

    }

    /* Returns the number of elements in the MinHeap. */
    public int size() {
        // TODO: YOUR CODE HERE
        return this.size;
    }

    /* Inserts ELEMENT into the MinHeap. If ELEMENT is already in the MinHeap,
       throw an IllegalArgumentException.*/
    public void insert(E element) {
        if (contains(element)) {
            throw new IllegalArgumentException();
        }
        contents.add(element);
        size += 1;
        int lastPlace = size;
//        contents.set(lastPlace, element);
        bubbleUp(lastPlace);

        // TODO: YOUR CODE HERE
    }

    /* Returns and removes the smallest element in the MinHeap. */
    public E removeMin() {
        // TODO: YOUR CODE HERE
        swap(1, size);
        contents.remove(size);
        size -= 1;
        bubbleDown(1);

        return null;
    }

    /* Replaces and updates the position of ELEMENT inside the MinHeap, which
       may have been mutated since the initial insert. If a copy of ELEMENT does
       not exist in the MinHeap, throw a NoSuchElementException. Item equality
       should be checked using .equals(), not ==. */
    public void update(E element) {
        if (contains(element)) {
            int index = findHelper(element, 1);
            E oldElement = getElement(index);
            setElement(index, element);
            if (element.compareTo(oldElement) > 0) {
                bubbleDown(index);
            }
            else if (element.compareTo(oldElement) < 0) {
                bubbleUp(index);
            }
        } else {
            throw new NoSuchElementException();
        }

        // TODO: YOUR CODE HERE
    }

    private int findHelper(E element, int index) {
        if (index == -1){
            return -1;
        }
        if (getElement(index).equals(element)){
            return index;
        }
        if (getElement(index).compareTo(element) > 0){
            return -1;
        }
        else{
            return Math.max(findHelper(element, getLeftOf(index)), findHelper(element, getRightOf(index)));
        }
    }


    /* Returns true if ELEMENT is contained in the MinHeap. Item equality should
       be checked using .equals(), not ==. */
    public boolean contains(E element) {
        // TODO: YOUR CODE HERE
        return containsHelper(element, 1);
    }

    private boolean containsHelper(E element, int index){
        if (contents.size() <= 1) {
            return false;
        }
        if (index == -1) {
            return false;
        }
        if (getElement(index).equals(element)) {
            return true;
        }
        if (getElement(index).compareTo(element) > 0) {
            return false;
        } else {
            return (containsHelper(element, getLeftOf(index)) || containsHelper(element, getRightOf(index)));
        }
    }
}
