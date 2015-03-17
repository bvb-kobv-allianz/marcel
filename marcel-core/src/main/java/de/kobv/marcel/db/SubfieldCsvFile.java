package de.kobv.marcel.db;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Subfield;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Writer;

/**
 *
 *
 */
public class SubfieldCsvFile extends CsvFile {

    private static final Logger LOG = Logger.getLogger(SubfieldCsvFile.class);

    public static final String SUBFIELD_PREFIX = "subfields_";

    private String tag;

    public void write(final String uid, final Datafield datafield) throws IOException {
        Writer out = getWriter();

        for (Subfield subfield : datafield.getSubfields()) {
            out.write(StringEscapeUtils.escapeCsv(uid));
            out.write(CSV_DELIMITER);
            out.write(Integer.toString(datafield.getTagIdx()));
            out.write(CSV_DELIMITER);
            out.write(datafield.getInd1());
            out.write(CSV_DELIMITER);
            out.write(datafield.getInd2());
            out.write(CSV_DELIMITER);
            try {
                write(subfield);
            }
            catch (StringIndexOutOfBoundsException e) {
                String message = "StringEscapeUtils.escapeCsv() error at uid: " + uid + ", tag: " + tag;
                System.err.println(message);
                LOG.error(message);
            }
            out.write(CSV_END_OF_LINE);
        }

        // TODO fileUtil.printToFile(SUBFIELD_PREFIX + getTag() + ".csv", subfieldsCSVs.get(tag).toString());
    }


    public void write(final Subfield subfield) throws IOException {
        Writer out = getWriter(); // TODO
        out.write(subfield.getCode());
        out.write(CSV_DELIMITER);
        try {
            out.write(StringEscapeUtils.escapeCsv(subfield.getValue()));
        }
        catch (StringIndexOutOfBoundsException e) {
            // TODO IMPORTANT analyse and document these exceptions
            String message = "StringEscapeUtils.escapeCsv() caused error by value " + subfield.getValue()
                    + " at code: " + subfield.getCode();
            System.err.println(message);
        }
    }

    public String getFilename() {
        return SUBFIELD_PREFIX + getTag() + ".csv";
    }

    public String getTag() {
        return tag;
    }

    public void setTag(final String value) {
        this.tag = value;
    }

}
