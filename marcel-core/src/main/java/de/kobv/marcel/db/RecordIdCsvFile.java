package de.kobv.marcel.db;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;

/**
 * all_uids.csv
 */
public class RecordIdCsvFile extends CsvFile {

    public void write(final String uid) throws IOException {
        Writer out = getWriter();
        out.write(StringEscapeUtils.escapeCsv(uid));
        out.write(CSV_END_OF_LINE);
    }

}
