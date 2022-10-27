import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapTest {

    @Test
    public void test1() {
        MinHeap<Integer> testHeap = new MinHeap<>();
        testHeap.insert(5);
        testHeap.insert(7);
        testHeap.insert(9);
        assertEquals(3, testHeap.size());
        assertTrue(testHeap.contains(5));
        assertFalse(testHeap.contains(10));

        testHeap.insert(2);
        testHeap.insert(3);
        testHeap.insert(6);
        testHeap.insert(8);
        testHeap.removeMin();
//        assertEquals(6, testHeap.size());

        System.out.println(testHeap.toString());
    }
}
