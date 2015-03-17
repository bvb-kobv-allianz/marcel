package de.kobv.marcel.main;

import de.kobv.marcel.db.ICsvImport;
import de.kobv.marcel.db.IMarcelDatabase;
import de.kobv.marcel.util.FileUtil;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Imports records and statistics into database.
 *
 * TODO review
 */
public class DatabaseImport {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(DatabaseImport.class);

    /**
     * Basis database methods.
     */
    private IMarcelDatabase dbMethods;

    /**
     * Should database be deleted before import?
     */
    private boolean dropDatabase = false;

    private boolean createDatabase = false;

    private Set<ICsvImport> csvImports = new HashSet<ICsvImport>();

    private FileUtil fileUtil;

    public void prepareImport() throws IOException {
        if (isDropDatabase()) {
            dbMethods.dropTables();
        }
        else {
            dbMethods.deleteDuplicateRecords(); // TODO review/refactor
        }

        if (isCreateDatabase()) {
            dbMethods.createDatabase(); // TODO only if necessary
        }

        dbMethods.createTables();
    }

    public void importToDatabase() throws IOException {
        LOG.debug("Importing CSV files ...");
        // TODO dbMethods.createStatistics(); remove
        for (ICsvImport csvFile : getCsvImports()) {
            csvFile.setPath(fileUtil.getTempPath()); // TODO do differently
            csvFile.importFile();
        }
    }

    public void finishImport() throws IOException, SQLException {
    }

    public IMarcelDatabase getDbMethods() {
        return dbMethods;
    }

    public void setDbMethods(final IMarcelDatabase methods) {
        this.dbMethods = methods;
    }

    public boolean isDropDatabase() {
        return dropDatabase;
    }

    public void setDropDatabase(final boolean value) {
        this.dropDatabase = value;
    }

    public boolean isCreateDatabase() {
        return createDatabase;
    }

    public void setCreateDatabase(final boolean value) {
        this.createDatabase = value;
    }

    public Set<ICsvImport> getCsvImports() {
        return csvImports;
    }

    public void setCsvImports(final Set<ICsvImport> imports) {
        this.csvImports = imports;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(final FileUtil futil) {
        this.fileUtil = futil;
    }
}
