package de.kobv.marcel.statistics;

import de.kobv.marcel.db.CsvFile;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Einfache Klasse für Fehlererfassung.
 *
 * TODO Weg finden init() und finish() aufrufen zu lassen ohne explizit Abhängigkeit in Control-Klassen (Main, ..)
 */
public class ErrorHandler extends CsvFile {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(ErrorHandler.class);

    private String path;

    private String filename;

    private Writer out;

    public void error(final String uid, final String code, final String message) {
        try {
            Writer wout = getWriter();
            wout.write(uid);
            wout.write(CSV_DELIMITER);
            wout.write(code);
            wout.write(CSV_DELIMITER);
            wout.write(StringEscapeUtils.escapeCsv(message));
            wout.write(CSV_END_OF_LINE);
        }
        catch (IOException ioe) {
            LOG.error(ioe);
            // TODO do something?
        }
    }

    public void finish() {
        try {
            if (out != null) {
                out.close();
            }
        }
        catch (IOException ioe) {
            LOG.error(ioe);
        }
    }

    public Writer getWriter() throws IOException {
        if (out == null) {
            out = Files.newBufferedWriter(Paths.get(getFilename()), Charset.forName("UTF8"));
        }
        return out;
    }

    public String getPath() {
        return path;
    }

    public void setPath(final String folder) {
        this.path = folder;
    }

    public String getFilename() {
        return filename + "_errors.csv";
    }

    public void setFilename(final String fname) {
        this.filename = fname;
    }

}
