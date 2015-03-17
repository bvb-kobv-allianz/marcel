package de.kobv.marcel.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;

import de.kobv.marcel.parser.IMarcParser;
import de.kobv.marcel.parser.MarcXMLParserException;

import de.kobv.marcel.statistics.ErrorHandler;
import de.kobv.marcel.statistics.MarcelReport;
import org.apache.log4j.Logger;

/**
 * Processes MARC file(s).
 *
 * Diese Klasse soll abhängig von Optionen auf der Kommandozeile MARC Dateien
 * - analysieren und eine Statistik erstellen
 * - CSV für den Datenbank import erstellen
 * - den Datenbank import durchführen
 */
public class Main {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(Main.class);

    /**
     * Statistics report.
     */
    private MarcelReport report;

    private ErrorHandler errorHandler;

    /**
     * MARC parser.
     */
    private IMarcParser parser;

    private Path[] files;

    private boolean importEnabled;

    private DatabaseImport databaseImport;

    /**
     * Counter for processed files.
     */
    private int fileIndex;

    /**
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws MarcXMLParserException
     */
    public void run() throws IOException, ClassNotFoundException, SQLException, MarcXMLParserException {
        fileIndex = 0;

        for (Path file : getFiles()) {
            processFile(file);
        }

        if (getReport() != null) {
            getReport().writeReport();
        }

        if (getErrorHandler() != null) {
            getErrorHandler().finish();
        }

        if (getDatabaseImport() != null && isImportEnabled()) {
            getDatabaseImport().prepareImport();
            getDatabaseImport().importToDatabase();
        }

    }

    /**
     * @param file
     * @throws IOException TODO auslagern?
     *                     TODO Fehlermeldungen überarbeiten
     */
    private void processFile(final Path file) throws IOException {
        if (Files.isRegularFile(file)) {
            fileIndex++;
            LOG.info("Parsing file # " + fileIndex + ": " + file.getFileName());
            try {
                parser.parse(getInputStream(file));
            }
            catch (MarcXMLParserException mxmlpe) {
                String message = "Parsing error in file " + file.toAbsolutePath() + ", record "
                        + mxmlpe.getRecordUid();
                LOG.error(message, mxmlpe);
            }
            catch (FileNotFoundException e) {
                LOG.error("Error digesting MarcXML: ", e);
            }
        }
        else {
            throw new FileNotFoundException("File '" + file.getFileName() + "' not found.");
        }
    }

    /**
     * Returns MARCXML inputstream for file.
     *
     * @return InputStream with MARCXML
     * @throws IOException
     */
    private InputStream getInputStream(final Path file) throws IOException {
        try {
            return Files.newInputStream(file);
        }
        catch (FileNotFoundException e) {
            // TODO review
            String errorMsg = "could not read MARC-XML file " + file + " : " + e.getMessage();
            LOG.error(errorMsg);
            throw e;
        }
    }

    public IMarcParser getParser() {
        return parser;
    }

    public void setParser(final IMarcParser marcParser) {
        this.parser = marcParser;
    }

    public MarcelReport getReport() {
        return report;
    }

    public void setReport(final MarcelReport mreport) {
        this.report = mreport;
    }

    public DatabaseImport getDatabaseImport() {
        return databaseImport;
    }

    public void setDatabaseImport(final DatabaseImport dbimport) {
        this.databaseImport = dbimport;
    }

    public boolean isImportEnabled() {
        return importEnabled;
    }

    public void setImportEnabled(final boolean value) {
        this.importEnabled = value;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(final ErrorHandler ehandler) {
        this.errorHandler = ehandler;
    }

    public Path[] getFiles() {
        return files;
    }

    public void setFiles(final Path[] filesVal) {
        this.files = filesVal;
    }

}