package de.kobv.marcel.db.mysql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

import de.kobv.marcel.beans.Record;
import de.kobv.marcel.db.CsvFile;
import de.kobv.marcel.db.IMarcelDatabase;
import de.kobv.marcel.util.FileUtil;
import org.apache.log4j.Logger;

import javax.sql.DataSource;

/**
 * Implementation of required database functions for MySQL.
 *
 * TODO cleanup/refactor
 */
public class DBMethods implements IMarcelDatabase {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(DBMethods.class);

    /**
     * Database access object.
     */
    private DataSource dataSource;

    /**
     * Name of database.
     */
    private String database;

    /**
     * TODO needed here?
     */
    private String csvDelimiter = ",";

    /**
     * TODO remove
     */
    private FileUtil fileUtil;

    /**
     * Names of statements for creating database tables.
     */
    private String[] tables = {
            "createRecordTable",
            "createNonValidTable",
            "createUidTable",
            "createControlfieldTable",
            "createFieldsTable",
            "createCategoriesTable",
            "createTrashTable"
    };

    public String getDatabase() {
        return database;
    }

    public void setDatabase(final String dbase) {
        this.database = dbase;
    }

    /**
     * SQL statements.
     */
    private ResourceBundle statements = ResourceBundle.getBundle("de.kobv.marcel.mysql.statements");

    /**
     * Returns the named SQL statement with placeholders replaced.
     * @param name Key for statement in properties file
     * @return SQL string
     */
    public String getSql(final String name) {
        String sql = statements.getString(name);
        return sql.replaceAll("<dataSource>", database);
    }

    /**
     * Removes the entire database.
     */
    public void dropDatabase() {
        String dropDatabaseDS = getSql("dropDatabase");

        Statement stmt = null;

        Connection conn = null;

        try {
            conn = getDataSource().getConnection();

            conn.setAutoCommit(false);

            stmt = conn.createStatement();

            LOG.info("dropping database " + database, null);
            stmt.execute(dropDatabaseDS);

            conn.commit();
        }
        catch (SQLException e) {
            LOG.error(e);

            try {
                conn.rollback();
            }
            catch (SQLException e1) {
                LOG.error(e1);
            }

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

    public void dropTables() {
        String getTables = getSql("dropTables");

        Statement stmt = null;

        Connection conn = null;

        try {
            conn = getDataSource().getConnection();

            conn.setAutoCommit(false);

            stmt = conn.createStatement();

            stmt.execute("SET FOREIGN_KEY_CHECKS=0");

            LOG.info("Dropping tables for " + database, null);
            ResultSet result = stmt.executeQuery(getTables);

            conn.commit();

            while (result.next()) {
                LOG.debug(result.getString(1));
                stmt = conn.createStatement();
                stmt.execute(result.getString(1));
            }

            stmt.execute("SET FOREIGN_KEY_CHECKS=1");

            conn.commit();
        }
        catch (SQLException e) {
            LOG.error(e);

            try {
                conn.rollback();
            }
            catch (SQLException e1) {
                LOG.error(e1);
            }
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

    /**
     * Creates database tables.
     *
     * TODO decentralize
     */
    public void createTables() {
        Statement stmt = null;

        Connection conn = null;

        try {
            conn = dataSource.getConnection();

            conn = getDataSource().getConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();

            LOG.info("Creating tables ...", null);

            for (String name : tables) {
                String sqlStr = getSql(name);
                LOG.debug(sqlStr);
                stmt.execute(sqlStr);
            }

            conn.commit();

        }
        catch (SQLException e) {
            LOG.error(e);

            try {
                conn.rollback();
            }
            catch (SQLException e1) {
                LOG.error(e1);
            }
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

    public boolean isDatabaseExisting() {
        try {
            Connection conn = dataSource.getConnection();

            ResultSet catalogs = conn.getMetaData().getCatalogs();

            while (catalogs.next()) {
                String databaseName = catalogs.getString(1);
                LOG.debug(databaseName);
                if (databaseName.equals(this.database)) {
                    return true;
                }
            }
        }
        catch (SQLException sqle) {
            LOG.error(sqle);
            // TODO do something
        }

        return false;
    }

    /**
     * Creates database tables.
     *
     * TODO decentralize
     */
    public void createDatabase() {
        Connection conn = null;

        Statement stmt = null;

        try {
            conn = dataSource.getConnection();

            conn = getDataSource().getConnection();
            conn.setAutoCommit(false);

            stmt = conn.createStatement();

            LOG.info("Creating database ...", null);

            stmt.execute(getSql("createDatabase"));

            conn.commit();

        }
        catch (SQLException e) {
            LOG.error(e);

            try {
                conn.rollback();
            }
            catch (SQLException e1) {
                LOG.error(e1);
            }
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

    /**
     * Inserts records into trash table.
     *
     * @param records
     * @throws UnsupportedEncodingException
     */
    public void insertTrash(final List<Record> records) throws UnsupportedEncodingException {
        if (records.size() == 0) {
            return;
        }
        String insertTrashDS = getSql("insertTrash");
        StringBuilder trashSql = new StringBuilder(insertTrashDS);
        String delimiter = "";

        LOG.info("inserting trash", null);

        try {
            Connection conn = getDataSource().getConnection();

            for (int i = 0; i < records.size(); i++) {
                trashSql.append(delimiter);
                trashSql.append("(?)");
                delimiter = ",";
            }

            PreparedStatement trashPstmt = conn.prepareStatement(trashSql.toString());

            int index = 1;

            for (Record record : records) {
                trashPstmt.setString(index++, record.toMarcXML(false));
            }

            trashPstmt.executeUpdate();

        }
        catch (SQLException e) {
            LOG.error(e);
        }
    }

    /**
     * Deletes selected records before importing updates.
     *
     * Reads records.csv, extracts the record identifiers and deletes the records in the database.
     *
     * TODO check that deletion is complete
     *
     * @throws UnsupportedEncodingException
     */
    public void deleteDuplicateRecords() throws UnsupportedEncodingException {
        String filesPath = getFileUtil().getTempPath().toString();

        File recordFile = new File(filesPath + "/records.csv");

        if (!(recordFile.exists() && recordFile.canRead())) {
            LOG.warn("Record file " + recordFile.getPath() + " doesn't exist or cannot be read. No records deleted.");
            return;
        }

        LOG.info("Deleting old duplicate records", null);

        BufferedReader reader;

        Connection conn = null;

        try {
            conn = getDataSource().getConnection();
            conn.setAutoCommit(false);

            String deleteRecordDS = getSql("deleteRecord");

            LOG.debug("deleteRecord = "  + deleteRecordDS);

            PreparedStatement deletePstmt = conn.prepareStatement(deleteRecordDS);

            reader = new BufferedReader(new FileReader(recordFile));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(CsvFile.CSV_DELIMITER);
                String uid = split[0];
                LOG.debug("Deleting record with id = " + uid);
                if (uid.length() > 0) {
                    deletePstmt.setString(1, uid);
                    deletePstmt.executeUpdate();
                }
            }
            conn.commit();
            deletePstmt.close();
        }
        catch (SQLException e) {
            LOG.error(e);

            try {
                conn.rollback();
            }
            catch (SQLException e1) {
                LOG.error(e1);
            }

        }
        catch (FileNotFoundException e) {
            LOG.error("", e);
        }
        catch (IOException e) {
            LOG.error("", e);
        }
    }

    public void createStatistics() {
        String numRecordsDS = getSql("numRecords");
        String prefixCatAllocationDS = getSql("prefixCatAllocation");
        String getA2CatAllocationDS = getSql("getA2CatAllocation");
        String a2categoriesDS = getSql("a2categories");

        PreparedStatement pstmt = null;

        Connection conn = null;

        try {
            conn = getDataSource().getConnection();

            LOG.info("Counting records", null);
            pstmt = conn.prepareStatement(numRecordsDS);
            pstmt.executeUpdate();

            /* Categories: Prefix */
            LOG.info("Creating prefix category statistics", null);
            pstmt = conn.prepareStatement(getSql("categories"));
            pstmt.setString(1, "prefix");
            ResultSet resultSet = pstmt.executeQuery();


            while (resultSet.next()) {
                String cat = resultSet.getString("cat");
                String field = resultSet.getString("field");
                String definition = resultSet.getString("definition");

                LOG.info("  Category: " + cat + ", field: " + field + ", definition: " + definition, null);

                if (field.length() != 4) {
                    LOG.warn("Invalid subfield name in category definition: " + field);
                    continue;
                }
                /* TODO get allocation
                if (!variables.isJustStats() && !variables.getFieldAllocations().keySet().contains(field)) {
                    continue;
                }*/
                String tag = field.substring(0, 3);
                String code = field.substring(3);

                String prefixCatAllocationF = prefixCatAllocationDS
                        .replace("<field>", field).replaceAll("<tag>", tag).replaceAll("<cat>", cat);
                PreparedStatement pstmtCat = conn.prepareStatement(prefixCatAllocationF);
                pstmtCat.setString(1, code);
                pstmtCat.setString(2, definition + "%");
                pstmtCat.setString(3, code);
                pstmtCat.setString(4, definition + "%");
                pstmtCat.executeUpdate();
            }

            /* Categories: a2 */
            LOG.info("Creating prefix category statistics", null);
            pstmt.setString(1, "a2");
            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                String field = resultSet.getString("field");

                LOG.info("  Field: " + field, null);

                if (field.length() != 4) {
                    LOG.warn("Invalid subfield name in category definition: " + field);
                    continue;
                }
                /* TODO get allocation
                if (!variables.isJustStats() && !variables.getFieldAllocations().keySet().contains(field)) {
                    continue;
                }*/
                String tag = field.substring(0, 3);

                String a2categoriesF = a2categoriesDS.replaceAll("<tag>", tag);
                Statement a2stmt = conn.createStatement();
                ResultSet cats = a2stmt.executeQuery(a2categoriesF);

                while (cats.next()) {
                    String thisCat = cats.getString("value");
                    LOG.info("  Category: " + thisCat, null);
                    String getA2CatAllocationF = getA2CatAllocationDS
                            .replace("<field>", field).replaceAll("<tag>", tag).replaceAll("<cat>", thisCat);
                    PreparedStatement pstmtCat = conn.prepareStatement(getA2CatAllocationF);
                    pstmtCat.setString(1, thisCat);
                    pstmtCat.setString(2, thisCat);
                    pstmtCat.executeUpdate();
                }
            }

        }
        catch (SQLException e) {
            LOG.error(e);
        }
        finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }

                conn.close();
            }
            catch (SQLException e) {
                LOG.error(e);
            }
        }
    }

    public String getCsvDelimiter() {
        return csvDelimiter;
    }

    public void setCsvDelimiter(final String delimiter) {
        this.csvDelimiter = delimiter;
    }

    public FileUtil getFileUtil() {
        return fileUtil;
    }

    public void setFileUtil(final FileUtil util) {
        this.fileUtil = util;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(final DataSource source) {
        this.dataSource = source;
    }

}