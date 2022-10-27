import org.junit.Test;

import static org.junit.Assert.*;

public class ModNCounterTest {

    int N = 7;
    @Test
    public void testConstructor() {
        ModNCounter c = new ModNCounter(N);
        assertEquals(N, c.value());
    }

    @Test
    public void increment() {
        ModNCounter c = new ModNCounter(N);
        c.increment();
        assertEquals(N+1, c.value());
        c.increment();
        assertEquals(N+2, c.value());
    }

    @Test
    public void reset() {
        ModNCounter c = new ModNCounter(N);
        c.increment();
        c.reset();
        assertEquals(0, c.value());
    }

}