package de.kobv.marcel.db;

import de.kobv.marcel.statistics.FieldAllocation;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

/**
 * TODO write to fields.csv
 */
public class FieldAllocationCsvFile extends CsvFile {

    private static final Logger LOG = Logger.getLogger(FieldAllocationCsvFile.class);

    public void write(final FieldAllocation fieldAllocation) throws IOException {
        Writer out = getWriter();
        out.write(fieldAllocation.getField());
        out.write(CSV_DELIMITER);
        out.write(Integer.toString(fieldAllocation.getAlloc()));
        out.write(CSV_DELIMITER);
        out.write(Integer.toString(fieldAllocation.getMultipleAlloc()));
        out.write(CSV_END_OF_LINE);
    }

    /**
     *
     * @param fieldAllocations Set<fieldAllocations>
     */
    public void write(final Collection<FieldAllocation> fieldAllocations) throws IOException {
        LOG.debug("Writing field allocations ...");
        for (FieldAllocation fieldAllocation : fieldAllocations) {
            write(fieldAllocation);
        }
    }

}
