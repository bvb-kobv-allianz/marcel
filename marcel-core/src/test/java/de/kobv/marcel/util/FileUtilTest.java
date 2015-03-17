package de.kobv.marcel.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 */
public class FileUtilTest {

    private FileUtil fileUtil;

    @Before
    public void setUp() {
        this.fileUtil = new FileUtil();
    }

    @Test
    public void testCreateTempDir() throws Exception {
        fileUtil.createTempDir();
        Path path = fileUtil.getTempPath();
        assertTrue(Files.exists(path));
    }

}
