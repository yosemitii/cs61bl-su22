package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */
public class LinkedListDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. Please do not import java.util.Deque here!*/

    public static LinkedListDeque<Integer> lld = new LinkedListDeque<Integer>();
    public static LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
    public void setUp() {
        lld = new LinkedListDeque<>();
        lld1 = new LinkedListDeque<>();
    }
    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

		assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
		lld.addFirst(0);

        assertFalse("lld1 should now contain 1 item", lld.isEmpty());

        lld = new LinkedListDeque<Integer>(); //Assigns lld equal to a new, clean LinkedListDeque!


    }

    /** Adds an item, removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {
        lld.addFirst(10);
        assertEquals((int) lld.get(0), 10);

        assertEquals(null, lld.removeFirst());
        assertEquals(0, lld.size());

    }
    /** Make sure that removing from an empty LinkedListDeque does nothing */
    @Test
    public void removeEmptyTest() {
        assertEquals(null, lld.removeFirst());
        assertEquals(0, lld.size());
    }
    /** Make sure your LinkedListDeque also works on non-Integer types */
    @Test
    public void multipleParamsTest() {
        Deque<String> lld1 = new LinkedListDeque<String>();
        lld1.addFirst("helloworld");
        assertEquals("helloworld", lld1.get(0));

    }
    /** Make sure that removing from an empty LinkedListDeque returns null */
    @Test
    public void emptyNullReturn() {
        assertEquals(null, lld.removeFirst());
    }
    /** TODO: Write tests to ensure that your implementation works for really large
     * numbers of elements, and test any other methods you haven't yet tested!
     */
    @Test
    public void sizeTest(){
        int i = 0;
        while (i < 10){
            lld.addFirst(i);
            lld.addLast(i);
            i += 1;
        }
        assertEquals(20,lld.size());
        lld.printDeque();
    }

    @Test
    public void largeScaleTest(){
        int i = 0;
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        int scale = 100;
        while (i < scale){
            lld.addLast(i);
            lld1.addFirst(scale-1-i);
            i += 1;
        }
        while (i < scale){
            assertEquals(i, (int) lld.get(i));
        }
        assertEquals(lld, lld1);
        //removeTest
        lld.removeFirst();
        lld.removeLast();
        assertEquals(scale - 2, lld.size());
        assertEquals(1, (int) lld.get(0));
        assertEquals(scale-2, (int) lld.get(scale-3));

        //recursive get
        assertEquals(scale-2, (int) lld.getRecursive(scale-3));
    }

    @Test
    public void printDequePrint(){
        lld.addFirst(1);
        lld.addFirst(3);
        lld.addLast(5);
        lld.printDeque();
    }

    @Test
    public void randAddRemoveTest(){
        //c002
        lld.addFirst(0);
        lld.addFirst(1);
        assertEquals(1, (int) lld.removeFirst());
        assertEquals(0, (int) lld.removeFirst());

        //c003
        lld.addFirst(0);
        lld.addFirst(1);
        assertEquals(0, (int) lld.removeLast());
        assertEquals(1, (int) lld.removeLast());

        //c004
        lld.addLast(0);
        assertEquals(0, (int) lld.removeLast());

        //c005
        lld.addLast(0);
        assertEquals(0, (int) lld.removeFirst());
    }

    @Test
    public void equalsTest(){
        Deque dq = new ArrayDeque();
        Deque lld1 = new LinkedListDeque();
        lld1.addFirst(1);
        lld1.addFirst(2);
        lld1.addFirst(3);

        dq.addFirst(1);
        dq.addFirst(2);
        dq.addFirst(3);
        dq.printDeque();
        lld1.printDeque();
        //assertTrue(lld.equals(dq));
        assertEquals(lld1, dq);

    }

}
