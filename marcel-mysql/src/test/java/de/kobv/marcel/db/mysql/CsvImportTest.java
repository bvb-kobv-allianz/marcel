package de.kobv.marcel.db.mysql;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by jens on 1/8/15.
 */
public class CsvImportTest {

    @Test
    public void testWindowsPathEscaping() {
        Path path = Paths.get("c:\\pfad\\file");


        System.out.println(path.toString().replace("\\", "\\\\"));
    }

}
