package de.kobv.marcel.statistics;

import de.kobv.marcel.beans.Controlfield;
import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;
import de.kobv.marcel.db.FieldAllocationCsvFile;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Collects statistics for field allocation.
 *
 * For every tag (control and data fields) it is counted how often it occurs. In addition should be counted ???
 *
 * TODO soll gezählt werden in wievielen Records ein Feld mehrfach vorkommt?
 * TODO soll gezählt werden was die maximale Zahl an Wiederholungen für ein Feld in einem Record war?
 */
public class FieldStatistics extends AbstractRecordProcessor {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(FieldStatistics.class);

    /**
     * For these tags the subfield codes should be counted as well.
     */
    public static final String[] A2_FIELDS = {"024", "084"};

    // private List<String> a2List = Arrays.asList(A2_FIELDS); TODO

    private Map<String, FieldAllocation> statistics = new HashMap<String, FieldAllocation>();

    private Map<String, FieldAllocation> a2allocations = new HashMap<String, FieldAllocation>();

    private Set<String> fields = new HashSet<String>();

    private Set<String> fieldsMultiple = new HashSet<String>();

    private boolean createAllocationCsv;

    private FieldAllocationCsvFile allocationCsv;


    @Override
    public void process(final Record record) {
        // count control field tags
        fields.clear();
        fieldsMultiple.clear();

        for (Controlfield controlField : record.getControlfields()) {
            addField(controlField.getTag());
        }

        // count data field tags
        for (Datafield dataField : record.getDatafields()) {
            // boolean checkA2 = a2List.contains(dataField.getTag()); // TODO
            for (Subfield subField : dataField.getSubfields()) {
                addField(dataField.getTag() + subField.getCode());
                // if (checkA2) { // TODO
                    // if (subField.getCode() == '2') {
                        // TODO addField(dataField.getTag() + ":" + subField.getValue());
                    // }
                // }
            }
        }
    }

    public void addField(final String field) {
        FieldAllocation fieldCount = statistics.get(field);
        if (fieldCount == null) {
            fieldCount = new FieldAllocation(field);
            statistics.put(field, fieldCount);
        }
        fieldCount.incTotal();
        if (!fields.contains(field)) {
            fields.add(field);
            fieldCount.incAlloc();
        }
        else if (!fieldsMultiple.contains(field)) {
            fieldsMultiple.add(field);
            fieldCount.incMultipleAlloc();
        }
    }

    @Override
    public void finish() {
        super.finish();
        LOG.debug("Finish");
        if (allocationCsv != null && isCreateAllocationCsv()) {
            LOG.debug(allocationCsv);
            try {
                allocationCsv.write(getFieldAllocations().values());
                allocationCsv.close();
            }
            catch (IOException ioe) {
                LOG.error(ioe);
                // TODO do something
            }
        }
    }

    public Map<String, FieldAllocation> getFieldAllocations() {
        return statistics;
    }

    public Map<String, FieldAllocation> getA2Allocations() {
        return a2allocations;
    }

    public boolean isCreateAllocationCsv() {
        return createAllocationCsv;
    }

    public void setCreateAllocationCsv(final boolean value) {
        this.createAllocationCsv = value;
    }

    public FieldAllocationCsvFile getAllocationCsv() {
        return allocationCsv;
    }

    public void setAllocationCsv(final FieldAllocationCsvFile csvFile) {
        this.allocationCsv = csvFile;
    }

}
