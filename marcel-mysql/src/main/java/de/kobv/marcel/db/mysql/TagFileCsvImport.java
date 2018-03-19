package de.kobv.marcel.db.mysql;

import de.kobv.marcel.db.SubfieldCsvFile;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 */
public class TagFileCsvImport extends CsvImport {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(TagFileCsvImport.class);

    @Override
    public void importFile() {
        // TODO get all files with SUBFIELD_PREFIX
        SortedSet <Path> files = getFiles();

        for (Path file : files) {
            if (Files.isRegularFile(file)) {
                String table = file.getFileName().toString();
                table = table.substring(0, table.indexOf('.'));
                setTableName(table);

                LOG.info("Creating table " + table, null);
                createTable();

                LOG.info("Load to table " + table, null);
                setFilename(file.getFileName().toString());
                super.importFile();
            }
        }
    }

    public void createTable() {
        String createSubfieldTableDS = getDbMethods().getSql("createSubfieldTable");

        Statement stmt = null;
        Connection conn = null;

        try {
            conn = getDbMethods().getDataSource().getConnection();
            stmt = conn.createStatement();
            stmt.execute(createSubfieldTableDS.replaceAll("<table>", getTableName()));
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

    /**
     * Returns File objects.
     * @return
     */
    public SortedSet<Path> getFiles() {
        LOG.debug("Get files from " + getPath());

        SortedSet<Path> files = new TreeSet<Path>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(getPath(), SubfieldCsvFile.SUBFIELD_PREFIX
                + "*.{csv}")) {
            for (Path entry : stream) {
                files.add(entry);
            }
        }
        catch (IOException ioex) {
            // TODO do something
        }
        catch (DirectoryIteratorException diex) {
            // TODO do something
        }

        /*
        File[] files = new File(getPath()).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
            if (name.startsWith(SubfieldCsvFile.SUBFIELD_PREFIX)
                    && name.endsWith(".csv")) {
                return true;
            }
            return false;
            }
        });*/

        return files;
    }

}
