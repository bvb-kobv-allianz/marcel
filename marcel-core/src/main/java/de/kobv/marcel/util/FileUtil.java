package de.kobv.marcel.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * Handles temporary files.
 */
public class FileUtil {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    /**
     * Path to folder for temporary files.
     */
    private Path tempPath;

    /**
     * Path to folder for
     */
    private String tempBasePath; // TODO filepath

    /**
     * Delete temporary directory when VM closes.
     */
    private boolean deleteTempOnExit = true;

    /**
     * Creates directory for temporary files.
     * @throws IOException
     */
    public void createTempDir() throws IOException {
        if (tempBasePath == null) {
            tempPath = Files.createTempDirectory("marcel");
        }
        else {
            tempPath = Files.createTempDirectory(FileSystems.getDefault().getPath(tempBasePath), "marcel");
        }
        if (isDeleteTempOnExit()) {
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    if (tempPath != null) {
                        try {
                            LOG.debug("Deleting temporary folder " + getTempPath().toFile());
                            FileUtils.deleteDirectory(getTempPath().toFile());
                        }
                        catch (IOException ioe) {
                            LOG.error("Temporary import folder could not be deleted.");
                        }
                    }
                }
            });
        }
        LOG.debug("Created temporary directory: " + tempPath);
    }

    public Path getTempPath() {
        return tempPath;
    }

    public boolean isDeleteTempOnExit() {
        return deleteTempOnExit;
    }

    public void setDeleteTempOnExit(final boolean deleteOnExit) {
        this.deleteTempOnExit = deleteOnExit;
    }

    public String getTempBasePath() {
        return tempBasePath;
    }

    public void setTempBasePath(final String tempBase) {
        this.tempBasePath = tempBase;
    }

    public static Set<Path> listFiles(Path path, String filter) {
        if (Files.isDirectory(path)) {
            // TODO do something
            return null;
        }

        Set<Path> files = new TreeSet<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(path, filter)) {
            for (Path entry : directoryStream) {
                files.add(entry);
            }
        }
        catch (DirectoryIteratorException diex) {
            // TODO do something
        }
        catch (IOException ioex) {
            // TODO do something
        }

        return files;
    }

}
