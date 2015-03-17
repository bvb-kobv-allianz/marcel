package de.kobv.marcel.statistics;

import java.io.PrintWriter;

/**
 * Created by jens on 2/27/14.
 */
public class AllocationFormat {

    private int totalDigits;

    private int digits;

    private int numberOfRecords;

    private String formatString;

    private static final long HUNDRED = 100L;

    public AllocationFormat(final int totalDigitsParam, final int noRecords) {
        this.totalDigits = totalDigitsParam;
        this.numberOfRecords = noRecords;
        this.digits = Integer.toString(numberOfRecords).length();
        this.formatString = "%1$-4s: %2$" + totalDigits + "d, %3$" + digits + "d (%4$3d%%), multiple: %5$"
                + digits + "d (%6$3d%%)";

    }


    /**
     *
     * @param writer
     *
     * TODO refactor (move out?)
     * TODO avoid repeated construction of format string for digits
     */
    public void format(final PrintWriter writer, final FieldAllocation allocation) {
        int alloc = allocation.getAlloc();
        int multipleAlloc = allocation.getMultipleAlloc();
        writer.println(String.format(formatString, allocation.getField(),
                allocation.getTotal(),
                alloc,
                HUNDRED * alloc / numberOfRecords,
                multipleAlloc,
                HUNDRED * multipleAlloc / numberOfRecords));
    }

}
