import org.junit.Test;
import static org.junit.Assert.*;

public class BooleanSetTest {

    @Test
    public void testBasics() {
        BooleanSet aSet = new BooleanSet(100);
        assertEquals(0, aSet.size());
        for (int i = 0; i < 100; i += 2) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }
        assertEquals(50, aSet.size());

        for (int i = 0; i < 100; i += 2) {
            aSet.remove(i);
            assertFalse(aSet.contains(i));
        }
        assertTrue(aSet.isEmpty());
        assertEquals(0, aSet.size());
    }
    @Test
    public void toIntArrayTest(){
        BooleanSet aSet = new BooleanSet(101);
        assertEquals(0, aSet.size());
        for (int i = 0; i < 100; i += 2) {
            aSet.add(i);
            assertTrue(aSet.contains(i));
        }
        int[] arr = aSet.toIntArray();
        for (int i = 0; i < aSet.size(); i++){
            assertEquals(i*2, arr[i]);
        }

        BooleanSet bSet = new BooleanSet(2);
        bSet.add(0);
        bSet.add(0);
        bSet.add(0);
        bSet.add(2);
        int[] arrB = bSet.toIntArray();
        assertEquals(0, arrB[0]);
        assertEquals(2, arrB[1]);
    }
    @Test
    public void testPrint(){
        BooleanSet aSet = new BooleanSet(100);
        aSet.add(5);
        aSet.add(5);
        aSet.add(7);
        aSet.add(9);
        aSet.add(10);
        aSet.add(12);
        int[] compare = {5, 7, 9, 10, 12};
        assertArrayEquals(aSet.toIntArray(), compare);
    }
}
