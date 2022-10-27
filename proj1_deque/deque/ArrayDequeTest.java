package deque;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/* Performs some basic array deque tests. */
public class ArrayDequeTest {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> ad = new ArrayDeque<Integer>();

    @Before
    public void setUp() {
        ad = new ArrayDeque<>();
    }

    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {

        assertTrue("A newly initialized ArrayDeque should be empty", ad.isEmpty());
        ad.addFirst(0);

        assertFalse("lld1 should now contain 1 item", ad.isEmpty());

        ad = new LinkedListDeque<Integer>(); //Assigns ad equal to a new, clean ArrayDeque!


    }

    /** Adds an item, removes an item, and ensures that dll is empty afterwards. */
    @Test
    public void addRemoveTest() {
        ad.addFirst(10);
        assertEquals((int) ad.get(0), 10);

        assertEquals(null, ad.removeFirst());
        assertEquals(0, ad.size());

    }
    /** Make sure that removing from an empty LinkedListDeque does nothing */
    @Test
    public void removeEmptyTest() {
        assertEquals(null, ad.removeFirst());
        assertEquals(0, ad.size());
    }
//    /** Make sure your LinkedListDeque also works on non-Integer types */
//    @Test
//    public void multipleParamsTest() {
//        Deque<String> ad1 = new ArrayDeque<String>("helloworld");
//        assertEquals("helloworld", ad1.get(0));
//
//    }
    /** Make sure that removing from an empty LinkedListDeque returns null */
    @Test
    public void emptyNullReturn() {
        assertEquals(null, ad.removeFirst());
    }

    @Test
    public void printDequePrint(){
        ad.addFirst(1);
        ad.addFirst(3);
        ad.addLast(5);
        ad.printDeque();
    }

    @Test
    public void ResizeTest(){
        for (int i = 0; i < 7; i++){
            ad.addFirst(i);
            ad.addLast(i);
        }
        ad.printDeque();
        ad.addFirst(7);
        ad.addLast(7);
        assertEquals(16, ad.size());
        //get test
//        for (int i = 0; i < 8; i++){
//            assertEquals(7-i, (int) ad.get(i));
//            assertEquals(i, (int) ad.get(i+8));
//        }
        assertEquals(7, (int) ad.get(0));
        assertEquals(0, (int) ad.get(8));

        //downsize
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        ad.removeFirst();
        assertEquals(7, ad.size());
        assertEquals(1, (int) ad.get(0));
        ad.addLast(7);
        ad.addLast(8);
        ad.addLast(9);
        ad.addFirst(0);
        ad.printDeque();
    }

    @Test
    public void equalsTest(){

        Deque ad1 = new ArrayDeque();
        ad1.addFirst(1);
        ad1.addFirst(2);
        ad1.addFirst(3);
        Deque lld = new LinkedListDeque();
        lld.addFirst(1);
        lld.addFirst(2);
        lld.addFirst(3);


        ad1.printDeque();
        lld.printDeque();
        assertEquals(ad1.size(), lld.size());
        boolean a = ad1.equals(lld);
        assertEquals(ad1, lld);
    }

}
