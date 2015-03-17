package de.kobv.marcel.parser;

import de.kobv.marcel.beans.Record;
import de.kobv.marcel.core.RecordMatcher;
import de.kobv.marcel.db.RecordIdCsvFile;
import de.kobv.marcel.statistics.IMarcRecordProcessor;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Set;

/**
 *
 * TODO move uidCsv Code out (database import code, should be module)?
 */
public class DefaultMarcParserHandler implements IMarcParserHandler {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(DefaultMarcParserHandler.class);

    private Set<IMarcRecordProcessor> processors;

    private RecordMatcher matcher = null;

    private boolean filterEnabled = false;

    private int numRecordsTotal = 0;

    private int numRecordsProcessed = 0;

    private int numRecordsWithoutUid = 0;

    private boolean importEnabled = false;

    private RecordIdCsvFile uidCsv = null;

    public void init() {
        for (IMarcRecordProcessor processor : getProcessors()) {
            processor.init();
        }
    }

    /**
     * Process MARC record.
     *
     * @param record
     * @throws IOException
     *
     * TODO set matcher to null if !isFilterLibraries?
     * TODO filter trash?
     */
    @Override
    public void processRecord(final Record record) throws IOException {
        if (record.getUid() != null) {
            numRecordsTotal++;
            if (matcher == null || !isFilterEnabled() || matcher.matches(record)) {
                numRecordsProcessed++;
                for (IMarcRecordProcessor processor : getProcessors()) {
                    processor.process(record);
                }
            }
            // TODO import all UIDs (to CSV)
            if (getUidCsv() != null && isImportEnabled()) {
                getUidCsv().write(record.getUid());
            }
        }
        else {
            // Record does not have UID => store separately
            // TODO trash.add(record);
            numRecordsWithoutUid++;
        }
    }

    @Override
    public void parsingFinished() throws IOException {
        LOG.debug("Finish Processing");
        for (IMarcRecordProcessor processor : getProcessors()) {
            LOG.debug("Call finish() on: " + processor);
            processor.finish();
        }
        LOG.debug(isImportEnabled());
        if (getUidCsv() != null && isImportEnabled()) {
            LOG.debug("Close UID file.");
            getUidCsv().close();
        }
    }

    public Set<IMarcRecordProcessor> getProcessors() {
        return processors;
    }

    public void setProcessors(final Set<IMarcRecordProcessor> rprocessors) {
        this.processors = rprocessors;
    }

    public RecordMatcher getMatcher() {
        return matcher;
    }

    public void setMatcher(final RecordMatcher rmatcher) {
        this.matcher = rmatcher;
    }

    public boolean isFilterEnabled() {
        return filterEnabled;
    }

    public void setFilterEnabled(final boolean value) {
        this.filterEnabled = value;
    }

    public int getNumRecordsTotal() {
        return numRecordsTotal;
    }

    public int getNumRecordsProcessed() {
        return numRecordsProcessed;
    }

    public int getNumRecordsWithoutUid() {
        return numRecordsWithoutUid;
    }

    public boolean isImportEnabled() {
        return importEnabled;
    }

    public void setImportEnabled(final boolean value) {
        this.importEnabled = value;
    }

    public RecordIdCsvFile getUidCsv() {
        return uidCsv;
    }

    public void setUidCsv(final RecordIdCsvFile ruidCsv) {
        this.uidCsv = ruidCsv;
    }
}
