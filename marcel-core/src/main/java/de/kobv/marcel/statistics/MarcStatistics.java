package de.kobv.marcel.statistics;

import java.util.Set;

import de.kobv.marcel.beans.Record;

/**
 * Generates statistics for a set of MARC records.
 */
public class MarcStatistics extends AbstractRecordProcessor {

    /**
     * Processors for analysing records.
     */
    private Set<IMarcRecordProcessor> processors;

    /**
     *
     * @param record
     */
    public void process(final Record record) {
        if (record == null) {
            throw new IllegalArgumentException("Argument record must not be null.");
        }

        for (IMarcRecordProcessor processor : getProcessors()) {
            processor.process(record);
        }
    }

    @Override
    public void finish() {
        super.finish();
        for (IMarcRecordProcessor processor : getProcessors()) {
            processor.finish();
        }
    }

    public Set<IMarcRecordProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(final Set<IMarcRecordProcessor> rprocessors) {
        this.processors = rprocessors;
    }

}
