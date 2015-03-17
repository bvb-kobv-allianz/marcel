package de.kobv.marcel.db;

import de.kobv.marcel.beans.Record;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;

/**
 * record.csv
 */
public class RecordCsvFile extends CsvFile {

    public void write(final Record record) throws IOException {
        Writer out = getWriter();
        out.write(StringEscapeUtils.escapeCsv(record.getUid()));
        out.write(CSV_DELIMITER);
        out.write(StringEscapeUtils.escapeCsv(record.getLeader()));
        out.write(CSV_DELIMITER);
        if (record.hasLibrary()) { // TODO not set in processing
            out.write('y');
        }
        else {
            out.write('n');
        }
        out.write(CSV_DELIMITER);
        if (record.isValid()) {  // TODO not set in processing
            out.write('y');
        }
        else {
            out.write('n');
        }
        out.write(CSV_END_OF_LINE);
    }
}
