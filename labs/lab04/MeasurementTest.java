import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class MeasurementTest {
    Measurement test = new Measurement(6,1);
    Measurement test2 = new Measurement(40);
    @Test
    public void testPlus() {
        // TODO: stub for first test
        Measurement a = test.plus(new Measurement(1,11));
        assertEquals(8,a.getFeet());
        assertEquals(0,a.getInches());
    }
    public void testMinus() {
        // TODO: stub for first test
        Measurement a = test.minus(test2);
        assertEquals(4,a.getFeet());
        assertEquals(2,a.getInches());
    }

    @Test
    public void testToString() {
        // TODO: stub for first test
        assertEquals("6'1\"",test.toString());
    }

    // TODO: Add additional JUnit tests for Measurement.java here.

}