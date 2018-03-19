package de.kobv.marcel.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class for storing command line parameters.
 *
 * Command line usage:
 *
 * <code>
 * marcel FILENAME
 * </code>
 *
 * Creates statistic for MARCXML file.
 *
 *
 * marcel -i "B3KAT" FILENAME
 *
 *
 */
@Parameters(resourceBundle = "de.kobv.marcel.cli.marcel-cli")
public class MarcelCmdLine {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(MarcelCmdLine.class);

    /**
     * Names of file/folder as command line argument.
     */
    @Parameter(descriptionKey = "arguments.description", required = true)
    private List<String> arguments = new ArrayList<String>();

    /**
     * Displays help information.
     */
    @Parameter(names = { "-h", "--help" }, descriptionKey = "option.help.description", help = true)
    private boolean showHelp = false;

    /**
     * Activates filtering for KOBV libraries.
     *
     * TODO refactor; general filtering (not KOBV specific)
     */
    @Parameter(names = { "-f", "--filter" }, descriptionKey = "option.filter.description")
    private boolean filter = false;

    @Parameter(names = { "-e", "--encoding" }, descriptionKey = "option.encoding.description")
    private String encoding;

    /**
     * Enables import to database.
     */
    @Parameter(names = { "-i", "--import" }, descriptionKey = "option.import.description")
    private boolean importEnabled = false;

    /**
     * Password for database access.
     */
    @Parameter(names = { "-p", "--password" }, descriptionKey = "option.password.description", password = true)
    private String password;

    /**
     * Username for database access.
     */
    @Parameter(names = { "-u", "--username" }, descriptionKey = "option.username.description")
    private String username;

    /**
     * Name of database schema.
     */
    @Parameter(names = { "--dbname" }, descriptionKey = "option.dbname.description")
    private String databaseName;

    /**
     * Name of database host, e.g. localhost, 127.0.0.1)
     */
    @Parameter(names = { "--dbhost" }, descriptionKey = "option.dbhost.description")
    private String databaseHost;

    /**
     * Port number for database connection.
     */
    @Parameter(names = { "--dbport" }, descriptionKey = "option.dbport.description")
    private String databasePort;

    /**
     * Drop database before import.
     */
    @Parameter(names = { "-D", "--drop" }, descriptionKey = "option.dropdatabase.description")
    private boolean dropDatabase = false;

    /**
     * Create database if necessary before import.
     */
    @Parameter(names = { "-C", "--create" }, descriptionKey = "option.createdatabase.description")
    private boolean createDatabase = false;

    /**
     * Enabled timestamp for names of output files.
     */
    @Parameter(names = { "-t", "--timestamp" }, descriptionKey = "option.timestamp.description")
    private boolean timestampEnabled = false;

    /**
     * Basename for output files.
     */
    @Parameter(names = { "-o", "--output" }, descriptionKey = "option.output.description")
    private String outputName = null; // TODO filename

    /**
     * Path for temporary files.
     */
    @Parameter(names = { "--temp" }, descriptionKey = "option.temp.description")
    private String tempPath; // TODO filename

    /**
     * Show version of application.
     */
    @Parameter(names = { "-v", "--version" }, descriptionKey = "option.version.description")
    private boolean showVersion = false;

    /**
     * Enables verbose debug output.
     */
    @Parameter(names = { "-X", "--debug" }, descriptionKey = "option.debug.description", hidden = true)
    private boolean debugEnabled;

    /**
     * Array of files for parsing.
     */
    private List<Path> files;

    /**
     * Directory for reading files.
     */
    private Path directory;

    /**
     * Timestamp string for output file names.
     */
    private String timestampString;

    /**
     * Basename for output files.
     */
    private String outputFileBasename;

    public boolean isShowHelp() {
        return showHelp;
    }

    public boolean isFilter() {
        return filter;
    }

    public boolean isImportEnabled() {
        return importEnabled;
    }

    public boolean isDropDatabase() {
        return dropDatabase;
    }

    public boolean isCreateDatabase() {
        return createDatabase;
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(final String dbHost) {
        this.databaseHost = dbHost;
    }

    public String getDatabasePort() {
        return databasePort;
    }

    public void setDatabasePort(final String dbPort) {
        this.databasePort = dbPort;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String pword) {
        this.password = pword;
    }

    public String getTempPath() {
        return tempPath;
    }

    public boolean isTimestampEnabled() {
        return timestampEnabled;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(final String dbName) {
        this.databaseName = dbName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String uname) {
        this.username = uname;
    }

    public boolean isShowVersion() {
        return showVersion;
    }

    public String getOutputName() {
        return outputName;
    }

    public final void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
    public final String getEncoding() {
        return this.encoding;
    }


    /**
     *
     * @return
     *
     * TODO refactor
     * TODO handle file/folder mix (not allowed)
     */
    public List<Path> getFiles() throws IOException {
        if (this.files == null) {
            List<Path> fileSet = new ArrayList<Path>();

            // if (arguments.size() == 0) {
                // TODO
            // }

            if (arguments.size() == 1) {
                // single argument, could be folder
                Path file = FileSystems.getDefault().getPath(arguments.get(0));

                /* TODO verify here?
                if (!file.exists()) {
                    throw new FileNotFoundException("File not found");
                }
                */

                if (Files.isDirectory(file)) {
                    LOG.debug(file + " is directory");
                    this.directory = file;

                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(file, "*.xml")) {
                        for (Path entry : stream) {
                            fileSet.add(entry);
                        }
                    }
                    catch (DirectoryIteratorException diex) {
                        LOG.error(diex);
                        // TODO do something
                        throw diex;
                    }
                }
                else {
                    fileSet.add(file);
                }
            }
            else {
                // multiple arguments
                for (String filename : arguments) {
                    fileSet.add(Paths.get(filename));
                }
            }

            this.files = fileSet;
        }

        return this.files;
    }

    /**
     * Returns basename for output files.
     * TODO what filename for directory?
     */
    public String getOutputFileBasename() {
        if (outputFileBasename == null) {
            List<Path> fileSet = null;

            try {
                fileSet = getFiles();
            }
            catch (FileNotFoundException fnfe) {
                LOG.error(fnfe);
            }
            catch (IOException ioex) {
                LOG.error(ioex);
            }

            String basename;

            if (getOutputName() != null) {
                basename = getOutputName();
            }
            else {
                if (directory != null) {
                    basename = directory.getFileName().toString();
                }
                else {
                    // use name of first file
                    // TODO generate file name?
                    if (fileSet != null && fileSet.size() > 0) {
                        basename = fileSet.get(0).getFileName().toString();
                    }
                    else {
                        basename = "notfound";
                    }
                }
            }

            if (isTimestampEnabled()) {
                basename += "_" + getTimestampString();
            }

            outputFileBasename = basename;
        }

        return outputFileBasename;
    }

    /**
     *
     * @return Timestamp string for output file names
     *
     * TODO timestamp format configurable
     */
    public String getTimestampString() {
        if (timestampString == null) {
            DateFormat df = new SimpleDateFormat("yyyyMMdd-HHmmss");
            timestampString = df.format(new Date());
        }
        return timestampString;
    }

    /**
     * TODO hide some keys (e.g. class, password, ...)
     */
    public void printValues() {
        Map<String, Object> values = null;

        try {
            values = PropertyUtils.describe(this);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        int maxKeyLength = 0;

        for (String key : values.keySet()) {
            if (key.length() > maxKeyLength) {
                maxKeyLength = key.length();
            }
        }

        SortedSet<String> sortedKeys = new TreeSet<String>(values.keySet());

        System.out.println("Command line/configuration options:");
        for (String key : sortedKeys) {
            System.out.println(String.format("  %1$" + maxKeyLength + "s = %2$s", key, values.get(key)));
        }
    }

    public String toString() {
        return StringUtils.replaceChars(this.getTempPath(), '\\', '/');
    }

}
