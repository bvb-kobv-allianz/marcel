package de.kobv.marcel.statistics;

/**
 *
 */
public class FieldAllocation {

    /**
     * Name of field.
     */
    private String field;

    /**
     * Counts how often the field occurred.
     */
    private int total = 0;

    /**
     * Counts in how many records the field occurred,
     */
    private int alloc = 0;

    /**
     * Counts in how many records the field occurred twice or more.
     */
    private int multipleAlloc = 0;

    public FieldAllocation(final String fieldName) {
        this.field = fieldName;
    }

    public String getField() {
        return field;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int value) {
        this.total = value;
    }

    public void incTotal() {
        this.total++;
    }

    public int getAlloc() {
        return alloc;
    }

    public void setAlloc(final int allocation) {
        this.alloc = allocation;
    }

    public void incAlloc() {
        this.alloc++;
    }

    public int getMultipleAlloc() {
        return multipleAlloc;
    }

    public void setMultipleAlloc(final int multiAlloc) {
        this.multipleAlloc = multiAlloc;
    }

    public void incMultipleAlloc() {
        this.multipleAlloc++;
    }

}
