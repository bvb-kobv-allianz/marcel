package de.kobv.marcel.statistics;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by jens on 2/27/14.
 */
public class FieldAllocationTest {

    private FieldAllocation field;

    @Before
    public void setUp() {
        this.field = new FieldAllocation("005");
    }

    @Test
    public void testIncTotal() {
        assertEquals(0, field.getTotal());
        field.incTotal();
        assertEquals(1, field.getTotal());
    }

    @Test
    public void testIncAlloc() {
        assertEquals(0, field.getAlloc());
        field.incAlloc();
        assertEquals(1, field.getAlloc());
    }

    @Test
    public void testIncMultipleAlloc() {
        assertEquals(0, field.getMultipleAlloc());
        field.incMultipleAlloc();
        assertEquals(1, field.getMultipleAlloc());
    }

}
