package de.kobv.marcel.db.mysql;

import de.kobv.marcel.db.ICsvImport;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Imports a CSV file into a MySQL database.
 *
 * TODO connection properties (inject connection?)
 * TODO database needed? or in connection?
 * TODO IMPORTANT modify design so that one instance of CsvImport is used for all files (different impl. for diff. db)
 */
public class CsvImport implements ICsvImport {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(CsvImport.class);

    /**
     * Database access object.
     */
    private DataSource dataSource;

    /**
     * Path to folder containing CSV file.
     */
    private Path path; // TODO use Path or File

    /**
     * Name of database.
     */
    private String database;

    /**
     * Name of CSV file.
     */
    private String filename;

    /**
     * Name of table for import
     */
    private String tableName;

    /**
     * Delimiter for values in CSV file.
     */
    private String csvDelimiter = ",";

    /**
     * Table columns.
     */
    private String columns;

    /**
     * Importing a file.
     */
    public void importFile() {
        LOG.info("Importing file " + getPath() + "/" + getFilename());

        Path file = getFile();

        if (!Files.isRegularFile(file)) {
            LOG.warn("File " + file.getFileName() + " not found.");
            return;
        }

        Statement stmt = null;
        Connection conn = null;

        try {
            conn = getDataSource().getConnection();
            stmt = conn.createStatement();
            String sql = getSql();
            LOG.debug("SQL = " + sql);
            stmt.execute(sql);
        }
        catch (SQLException e) {
            LOG.error(e);
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }

                conn.close();
            }
            catch (SQLException e) {
                LOG.error(e);
            }
        }
    }

    public Path getFile() {
        return getPath().resolve(getFilename());
    }

    /**
     * TODO use Path
     * @return
     */
    public String getSql() {
        String loadStatementSuffix = " CHARACTER SET utf8 COLUMNS TERMINATED BY '" + getCsvDelimiter()
                + "' OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\n'";

        String sqlStr = "LOAD DATA LOCAL INFILE '" + getPath().resolve(getFilename()).toString() + "' INTO TABLE "
                + getDatabase() + "." + getTableName() +  loadStatementSuffix + " (" + getColumns() + ")";

        LOG.debug("Generating import SQL for file: " + getPath().resolve(getFilename()).toString());

        return sqlStr.replace("\\", "\\\\");
    }

    public Path getPath() {
        return path;
    }

    public void setPath(final Path folder) {
        this.path = folder;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(final String dbase) {
        this.database = dbase;
    }

    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    public void setCsvDelimiter(final String delimiter) {
        this.csvDelimiter = delimiter;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(final String name) {
        this.filename = name;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(final String table) {
        this.tableName = table;
    }

    public String getColumns() {
        return columns;
    }

    public void setColumns(final String cols) {
        this.columns = cols;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(final DataSource source) {
        this.dataSource = source;
    }

}
