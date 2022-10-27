/* A doubly-linked list supporting various sorting algorithms. */
public class DLList<T extends Comparable<T>> {

    private class Node {

        T item;
        Node prev;
        Node next;

        Node(T item) {
            this.item = item;
            this.prev = this.next = null;
        }

        Node(T item, Node prev, Node next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    /* The sentinel of this DLList. */
    Node sentinel;
    /* The number of items in this DLList. */
    int size;

    /* Creates an empty DLList. */
    public DLList() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
    }

    /* Creates a copy of DLList represented by LST. */
    public DLList(DLList<T> lst) {
        Node ptr = lst.sentinel.next;
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        this.size = 0;
        while (ptr != lst.sentinel) {
            addLast(ptr.item);
            ptr = ptr.next;
        }
    }

    /* Returns true if this DLList is empty. Otherwise, returns false. */
    public boolean isEmpty() {
        return size == 0;
    }

    /* Adds a new Node with item ITEM to the front of this DLList. */
    public void addFirst(T item) {
        Node newNode = new Node(item, sentinel, sentinel.next);
        sentinel.next.prev = newNode;
        sentinel.next = newNode;
        size += 1;
    }

    /* Adds a new Node with item ITEM to the end of this DLList. */
    public void addLast(T item) {
        Node newNode = new Node(item, sentinel.prev, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    /* Removes the Node referenced by N from this DLList. */
    private void remove(Node n) {
        n.prev.next = n.next;
        n.next.prev = n.prev;
        n.next = null;
        n.prev = null;
        size -= 1;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            s.append(ptr.item.toString());
            s.append(" ");
        }
        return s.toString();
    }

    /* Returns a copy of this DLList sorted using insertion sort. Does not
       modify the original DLList. */
    public DLList<T> insertionSort() {
        DLList<T> toReturn = new DLList<>();
        for (Node ptr = sentinel.next; ptr != sentinel; ptr = ptr.next) {
            toReturn.insertionSortHelper(ptr.item);
        }
        return toReturn;
    }

    /* Inserts ITEM into this DLList such that the values of this DLList are in
       increasing order. */
    private void insertionSortHelper(T item) {
        // TODO: YOUR CODE HERE
//        Node ptr = sentinel.next;
//        while (!ptr.item.equals(item)) {
//            ptr = ptr.next;
//        }
//        while (ptr.item.compareTo(ptr.prev.item) < 0) {
//            T temp = ptr.prev.item;
//            ptr.prev.item = ptr.item;
//            ptr.item = temp;
//            ptr = ptr.prev;
//        }
        addLast(item);
        Node ptr = sentinel.prev;
        while (ptr.prev != sentinel) {
            if (ptr.item.compareTo(ptr.prev.item) < 0) {
                T temp = ptr.prev.item;
                ptr.prev.item = ptr.item;
                ptr.item = temp;
                ptr = ptr.prev;
            }
            else {
                return;
            }
        }
    }

    /* Returns a copy of this DLList sorted using selection sort. Does not
       modify the original DLList. */
    public DLList<T> selectionSort() {
        DLList<T> copy = new DLList<>(this);
        DLList<T> toReturn = new DLList<>();
        while (copy.size != 0) {
            Node min = copy.sentinel.next;
            for (Node p = copy.sentinel.next; p != copy.sentinel; p = p.next) {
                if (p.item.compareTo(min.item) < 0) {
                    min = p;
                }
            }
            toReturn.addLast(min.item);
            copy.remove(min);
        }
        return toReturn;
    }

    /* Returns a copy of this DLList sorted using merge sort. Does not modify
       the original DLList. */
    public DLList<T> mergeSort() {
        if (size <= 1) {
            return this;
        }
        DLList<T> oneHalf = new DLList<>();
        DLList<T> otherHalf = new DLList<>();
        // TODO: YOUR CODE HERE
        Node ptr = this.sentinel.next;
        Node ptr1 = this.sentinel.prev;
//        boolean flag = true;
//        while (ptr.next != ptr1.prev || ptr.next != ptr1) {
////            System.out.println(ptr.item.toString() + ptr1.item.toString());
////            if (flag == true) {
//                oneHalf.addLast(ptr.item);
//                ptr = ptr.next;
////                flag = false;
////                continue;
////            } else {
//                otherHalf.addFirst(ptr1.item);
//                ptr1 = ptr1.prev;
////                flag = true;
////                continue;
////            }
//        }
//        if (ptr.next == ptr1.prev) {
//            oneHalf.addLast(ptr.next.item);
//        }
//        if (flag == false) {
//            otherHalf.addFirst(ptr1.item);
//        }
//        } else {
//            oneHalf.addLast(ptr.item);
//        }
//        int count = 0;
//        while (ptr != sentinel) {
//            count += 1;
//            ptr = ptr.next;
//        }
//        ptr = sentinel.next;
        for (int i = 0; i < size / 2; i++) {
            oneHalf.addLast(ptr.item);
            otherHalf.addFirst(ptr1.item);
            ptr = ptr.next;
            ptr1 = ptr1.prev;
        }
        if (size % 2 == 1) {
            oneHalf.addLast(ptr.item);
        }
        DLList<T> oneResult = oneHalf.mergeSort();
        DLList<T> otherResult = otherHalf.mergeSort();
        DLList<T> result = new DLList<>();
        Node ptr2 = oneResult.sentinel.next;
        Node ptr3 = otherResult.sentinel.next;
        while (ptr2 != oneResult.sentinel && ptr3 != otherResult.sentinel) {
            if (ptr2.item.compareTo(ptr3.item) <= 0) {
                result.addLast(ptr2.item);
                ptr2 = ptr2.next;
            } else {
                result.addLast(ptr3.item);
                ptr3 = ptr3.next;
            }
        }
        if (ptr2 == oneResult.sentinel && ptr3 != otherResult.sentinel) {
            while (ptr3 != otherResult.sentinel) {
                result.addLast(ptr3.item);
                ptr3 = ptr3.next;
            }
        } else if (ptr2 != oneResult.sentinel && ptr3 == otherResult.sentinel) {
            while (ptr2 != oneResult.sentinel) {
                result.addLast(ptr2.item);
                ptr2 = ptr2.next;
            }
        }
        return result;
//        boolean flag = true;
//        while (ptr != sentinel) {
//            if (flag == true) {
//                oneHalf.addLast(ptr.item);
//                flag = false;
//            } else {
//                otherHalf.addLast(ptr.item);
//                flag = true;
//            }
//        }
//        return null;
    }

    /* Returns the result of merging this DLList with LST. Does not modify the
       two DLLists. Assumes that this DLList and LST are in sorted order. */
    private DLList<T> merge(DLList<T> lst) {
        DLList toReturn = new DLList();
        Node thisPtr = sentinel.next;
        Node lstPtr = lst.sentinel.next;
        while (thisPtr != sentinel && lstPtr != lst.sentinel) {
            if (thisPtr.item.compareTo(lstPtr.item) < 0) {
                toReturn.addLast(thisPtr.item);
                thisPtr = thisPtr.next;
            } else {
                toReturn.addLast(lstPtr.item);
                lstPtr = lstPtr.next;
            }
        }
        while (thisPtr != sentinel) {
            toReturn.addLast(thisPtr.item);
            thisPtr = thisPtr.next;
        }
        while (lstPtr != lst.sentinel) {
            toReturn.addLast(lstPtr.item);
            lstPtr = lstPtr.next;
        }
        return toReturn;
    }

    /* Returns a copy of this DLList sorted using quicksort. The first element
       is used as the pivot. Does not modify the original DLList. */
    public DLList<T> quicksort() {
        if (size <= 1) {
            return this;
        }
        // Assume first element is the divider.
        DLList<T> smallElements = new DLList<>();
        DLList<T> largeElements = new DLList<>();
        DLList<T> equalElements = new DLList<>();
        T pivot = sentinel.next.item;
        // TODO: YOUR CODE HERE
        Node ptr = sentinel.next;
        while (ptr != sentinel) {
            if (ptr.item.compareTo(pivot) > 0) {
                largeElements.addLast(ptr.item);
            }
            else if (ptr.item.compareTo(pivot) < 0) {
                smallElements.addLast(ptr.item);
            }
            else {
                equalElements.addLast(ptr.item);
            }
            ptr = ptr.next;
        }
        largeElements = largeElements.quicksort();
        smallElements = smallElements.quicksort();
        smallElements.append(equalElements);
        smallElements.append(largeElements);
        return smallElements;
    }

    /* Appends LST to the end of this DLList. */
    public void append(DLList<T> lst) {
        if (lst.isEmpty()) {
            return;
        }
        if (isEmpty()) {
            sentinel = lst.sentinel;
            size = lst.size;
            return;
        }
        sentinel.prev.next = lst.sentinel.next;
        lst.sentinel.next.prev = sentinel.prev;
        sentinel.prev = lst.sentinel.prev;
        lst.sentinel.prev.next = sentinel;
        size += lst.size;
    }

    /* Returns a random integer between 0 and 99. */
    private static int randomInt() {
        return (int) (100 * Math.random());
    }

    private static DLList<Integer> generateRandomIntegerDLList(int N) {
        DLList<Integer> toReturn = new DLList<>();
        for (int k = 0; k < N; k++) {
            toReturn.addLast((int) (100 * Math.random()));
        }
        return toReturn;
    }

    public static void main(String[] args) {
        DLList<Integer> values;
        DLList<Integer> sortedValues;

        System.out.print("Before insertion sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.insertionSort();
        System.out.print("After insertion sort: ");
        System.out.println(sortedValues);

        System.out.print("Before selection sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.selectionSort();
        System.out.print("After selection sort: ");
        System.out.println(sortedValues);

        System.out.print("Before merge sort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.mergeSort();
        System.out.print("After merge sort: ");
        System.out.println(sortedValues);

        System.out.print("Before quicksort: ");
        values = generateRandomIntegerDLList(10);
        System.out.println(values);
        sortedValues = values.quicksort();
        System.out.print("After quicksort: ");
        System.out.println(sortedValues);
    }
}