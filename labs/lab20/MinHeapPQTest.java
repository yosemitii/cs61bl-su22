import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void test1() {
        MinHeapPQ<Character> pq = new MinHeapPQ<>();
        pq.insert('c', 3.0);
        pq.insert('i', 9.0);
        pq.insert('g', 7.0);
        pq.insert('d', 40.0);
        pq.insert('a', 1.0);
        pq.insert('h', 8.0);
        pq.insert('e', 5.0);
        pq.insert('b', 2.0);
        assertFalse(pq.contains('x'));
//        assertTrue(pq.contains('i'));
//        System.out.println(pq);
//        pq.insert('d', 4.0);
        pq.changePriority('d', 4.0);
        pq.poll();
        pq.poll();
        pq.poll();
        pq.poll();
        pq.poll();
        pq.poll();
        pq.poll();
        pq.poll();

//        assertFalse(pq.contains('x'));
        System.out.println(pq);

    }
}
