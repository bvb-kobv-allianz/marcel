package de.kobv.marcel.db;

import de.kobv.marcel.beans.Record;
import de.kobv.marcel.statistics.IMarcRecordProcessor;
import de.kobv.marcel.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 *
 * Es werden mehrere Dateien generiert.
 *
 * - all_uids.csv TODO create somewhere else
 * - record.csv
 * - non_valid.csv TODO was not implemented, is it?
 * - controlfield.csv
 *
 * TODO use Set<CvsFile> ?
 * TODO find better way of handling enabled/disabled (by command line option)
 */
public class DatabaseImport implements IMarcRecordProcessor {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(DatabaseImport.class);

    /**
     * Modul enabled/disabled for processing.
     */
    private boolean enabled;

    private FileUtil fileUtil;

    /**
     * CSV File for records information.
     */
    private RecordCsvFile recordCsv;

    private ControlFieldCsvFile controlFieldCsv;

    private SubfieldCsvFiles dataFieldCsv;

    private boolean initialized = false;

    @Override
    public void init() {
        if (initialized || !isEnabled()) {
            return;
        }

        try {
            fileUtil.createTempDir();
            initialized = true;
        }
        catch (IOException ioe) {
            LOG.error(ioe);
            // TODO do something
        }
    }

    @Override
    public void process(final Record record) {
        if (!isEnabled()) {
            return;
        }

        try {
            recordCsv.write(record);
            controlFieldCsv.write(record);
            dataFieldCsv.write(record);
        }
        catch (IOException ioe) {
            LOG.error("Error writing record to CSV files.", ioe);
            // TODO do something ?
        }
    }

    @Override
    public void finish() {
        if (!isEnabled()) {
            return;
        }

        try {
            recordCsv.close();
            controlFieldCsv.close();
            dataFieldCsv.close();
        }
        catch (IOException ioe) {
            LOG.error(ioe);
            // TODO do something?
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean value) {
        this.enabled = value;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(final FileUtil futil) {
        this.fileUtil = futil;
    }

    public RecordCsvFile getRecordCsv() {
        return recordCsv;
    }

    public void setRecordCsv(final RecordCsvFile rCsv) {
        this.recordCsv = rCsv;
    }

    public ControlFieldCsvFile getControlFieldCsv() {
        return controlFieldCsv;
    }

    public void setControlFieldCsv(final ControlFieldCsvFile cfCsv) {
        this.controlFieldCsv = cfCsv;
    }

    public SubfieldCsvFiles getDataFieldCsv() {
        return dataFieldCsv;
    }

    public void setDataFieldCsv(final SubfieldCsvFiles dfCsv) {
        this.dataFieldCsv = dfCsv;
    }

}