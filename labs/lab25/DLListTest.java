import org.junit.Test;

public class DLListTest {
    @Test
    public void insertionSortTest() {
        DLList<Integer> dll = new DLList<>();
        dll.addLast(5);
        dll.addLast(1);
        dll.addLast(6);
        dll.addLast(8);
        dll.addLast(3);
        dll.addLast(2);
        dll.addLast(4);
        dll.addLast(7);
        DLList<Integer> result = dll.insertionSort();
        System.out.println(result.toString());
    }

    @Test
    public void mergeSortTest() {
        DLList<Integer> dll = new DLList<>();
        dll.addLast(5);
        dll.addLast(1);
        dll.addLast(6);
        dll.addLast(8);
        dll.addLast(3);
        dll.addLast(2);
        dll.addLast(4);
        dll.addLast(7);
        DLList<Integer> result = dll.mergeSort();
        System.out.println(result.toString());
    }

    @Test
    public void quickSortTest() {
        DLList<Integer> dll = new DLList<>();
        dll.addLast(5);
        dll.addLast(1);
        dll.addLast(6);
        dll.addLast(8);
        dll.addLast(3);
        dll.addLast(2);
        dll.addLast(4);
        dll.addLast(7);
        DLList<Integer> result = dll.quicksort();
        System.out.println(result.toString());
    }
}