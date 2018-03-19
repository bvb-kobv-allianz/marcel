package de.kobv.marcel.db;

import de.kobv.marcel.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;

/**
 * Abstract basis class for CSV files used for database import.
 *
 * TODO close writer
 * TODO review design
 */
public abstract class CsvFile {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(CsvFile.class);

    /**
     * Delimiter between values in CSV file.
     */
    public static final String CSV_DELIMITER = ",";

    /**
     * Line end for CSV file.
     */
    public static final String CSV_END_OF_LINE = "\n";

    /**
     * Writer for output.
     */
    private Writer out;

    /**
     * Name of file.
     */
    private String filename;

    /**
     * Utility class for file handling.
     */
    private FileUtil fileUtil;

    /**
     * Encoding.
     */
    private String encoding = "utf8";


    /**
     * Returns writer for output into CSV file.
     * @return Writer
     */
    public Writer getWriter() throws IOException {
        if (out == null) {
            LOG.debug(fileUtil.getTempPath() + "/" + getFilename());
            out = Files.newBufferedWriter(fileUtil.getTempPath().resolve(getFilename()),
                    Charset.forName(getEncoding()),
                    new OpenOption[] { StandardOpenOption.APPEND, StandardOpenOption.CREATE } );
        }
        return out;
    }

    public void close() throws IOException {
        if (out != null) {
            out.close();
            out = null;
        }
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String fname) {
        this.filename = fname;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(final FileUtil futil) {
        this.fileUtil = futil;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return this.encoding;
    }

}
