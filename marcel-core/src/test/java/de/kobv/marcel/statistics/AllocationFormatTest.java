package de.kobv.marcel.statistics;

import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by jens on 3/3/14.
 */
public class AllocationFormatTest {

    @Test
    public void testAddToStatisticsDocument() throws Exception {
        FieldAllocation allocation = new FieldAllocation("100s");

        allocation.setTotal(200);
        allocation.setAlloc(200);
        allocation.setMultipleAlloc(50);

        AllocationFormat format = new AllocationFormat(3, 200);

        StringWriter out = new StringWriter();

        format.format(new PrintWriter(out), allocation);

        out.close();

        assertEquals("100s: 200, 200 (100%), multiple:  50 ( 25%)", StringUtils.trim(out.toString()));
    }

    @Test
    public void testAddToStatisticsDocument2() throws Exception {
        FieldAllocation allocation = new FieldAllocation("006");

        allocation.setTotal(50);
        allocation.setAlloc(1);
        allocation.setMultipleAlloc(1);

        AllocationFormat format = new AllocationFormat(2, 97);

        StringWriter out = new StringWriter();

        format.format(new PrintWriter(out), allocation);

        out.close();

        assertEquals("006 : 50,  1 (  1%), multiple:  1 (  1%)", StringUtils.trim(out.toString()));
    }

    /**
     * Regression test for MARCEL-35
     */
    @Test
    public void testPercentForLargeNumbers() throws Exception {
        FieldAllocation allocation = new FieldAllocation("001");
        allocation.setTotal(24411322);
        allocation.setAlloc(24411322);
        allocation.setMultipleAlloc(0);

        AllocationFormat format = new AllocationFormat(8, 24411322);

        StringWriter out = new StringWriter();

        format.format(new PrintWriter(out), allocation);

        out.close();

        assertNotEquals("001 : 24411322, 24411322 (-75%), multiple:        0 (  0%)", StringUtils.trim(out.toString()));
        assertEquals("001 : 24411322, 24411322 (100%), multiple:        0 (  0%)", StringUtils.trim(out.toString()));
    }

}
