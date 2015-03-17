package de.kobv.marcel.db;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.util.FileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Eine separate Datei fuer jedes Tag.
 */
public class SubfieldCsvFiles {

    /**
     * CSV files for data field tags.
     */
    private Map<String, SubfieldCsvFile> subfieldFiles = new HashMap<String, SubfieldCsvFile>();

    private FileUtil fileUtil;

    /**
     *
     * @param record
     * @throws IOException
     */
    public void write(final Record record) throws IOException {
        for (Datafield datafield : record.getDatafields()) {
            SubfieldCsvFile subfieldFile = getCsvFileForTag(datafield.getTag());
            subfieldFile.write(record.getUid(), datafield);
        }
    }

    public SubfieldCsvFile getCsvFileForTag(final String tag) {
        SubfieldCsvFile subfieldFile = subfieldFiles.get(tag);
        if (subfieldFile == null) {
            subfieldFile = new SubfieldCsvFile();
            subfieldFile.setTag(tag);
            subfieldFile.setFileUtil(getFileUtil());
            subfieldFiles.put(tag, subfieldFile);
        }
        return subfieldFile;
    }

    public void close() throws IOException {
        for (SubfieldCsvFile subfieldFile : subfieldFiles.values()) {
            subfieldFile.close();
        }
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(final FileUtil futil) {
        this.fileUtil = futil;
    }

}
