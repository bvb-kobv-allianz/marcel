package de.kobv.marcel.db.mysql;

import static org.junit.Assert.*;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.SortedSet;

/**
 */
public class TagFileCsvImportTest {

    private TagFileCsvImport csvImport;

    private Path tempDir;

    @Before
    public void setUp() throws IOException {
        csvImport = new TagFileCsvImport();

        tempDir = Files.createTempDirectory("tagfiletest");

        csvImport.setPath(tempDir);

        Files.createFile(tempDir.resolve("subfields_test1.csv"));
        Files.createFile(tempDir.resolve("subfields_test3.csv"));
        Files.createFile(tempDir.resolve("subfields_test2.csv"));
    }

    @After
    public void tearDown() throws IOException {
        if (tempDir != null) {
            FileUtils.deleteDirectory(tempDir.toFile());
        }
    }

    @Test
    public void testGetFiles() {
        SortedSet<Path> files = csvImport.getFiles();
        assertEquals(3, files.size());
        assertEquals("subfields_test1.csv", files.first().getFileName().toString());
        assertEquals("subfields_test3.csv", files.last().getFileName().toString());
    }

}
