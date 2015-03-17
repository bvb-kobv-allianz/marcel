package de.kobv.marcel.db;

import de.kobv.marcel.beans.Controlfield;
import de.kobv.marcel.beans.Record;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;

/**
 * controlfield.csv
 */
public class ControlFieldCsvFile extends CsvFile {

    public void write(final Record record) throws IOException {
        String uid =  StringEscapeUtils.escapeCsv(record.getUid());
        for (Controlfield field : record.getControlfields()) {
            Writer out = getWriter();
            out.write(uid);
            out.write(CSV_DELIMITER);
            out.write(field.getTag());
            out.write(CSV_DELIMITER);
            out.write(StringEscapeUtils.escapeCsv(field.getValue()));
            out.write(CSV_END_OF_LINE);
        }
    }

}
