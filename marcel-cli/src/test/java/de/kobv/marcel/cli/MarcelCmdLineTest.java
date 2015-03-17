package de.kobv.marcel.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by jens on 2/7/14.
 */
public class MarcelCmdLineTest {

    private MarcelCmdLine cmdLine;

    @Before
    public void setUp() {
        this.cmdLine = new MarcelCmdLine();
    }

    @Test(expected = ParameterException.class)
    public void testArgumentsRequired() {
        String[] args = {};

        new JCommander(cmdLine, args);
    }

    @Test
    public void testHelpOption() {
        new JCommander(cmdLine, "-h");

        assertTrue(cmdLine.isShowHelp());
    }

    @Test
    public void testRequiredOptionsShort() throws Exception {
        String[] args = {"testfile"};

        new JCommander(cmdLine, args);

        assertTrue(cmdLine.getFiles().size() == 1);
    }

    @Test
    public void testOutputFileBasename() {
        String[] args = {"testfile.dat"};

        new JCommander(cmdLine, args);

        assertEquals("testfile.dat", cmdLine.getOutputFileBasename());
    }

    @Test
    public void testOutputFileBasenameWithTimestamp() {
        String[] args = {"-t", "testfile.dat"};

        new JCommander(cmdLine, args);

        String filename = cmdLine.getOutputFileBasename();

        assertTrue(filename, filename.matches("testfile.dat_\\d{8}-\\d{6}"));

        // additional calls return same value
        assertEquals(filename, cmdLine.getOutputFileBasename());
    }

    @Test
    public void testDropDatabaseParameterTrue() {
        String[] args = {"--drop", "testfile"};

        new JCommander(cmdLine, args);

        assertTrue(cmdLine.isDropDatabase());
    }

    @Test
    public void testDropDatabaseParameterFalse() {
        String[] args = new String[] {"testfile"};

        new JCommander(cmdLine, args);

        assertFalse(cmdLine.isDropDatabase());
    }

    @Test
    public void testSourceParameter() {
        String[] args = {"--dbname", "marceldb", "test.xml"};

        new JCommander(cmdLine, args);

        assertEquals("marceldb", cmdLine.getDatabaseName());
    }

    @Test
    public void testImportParameter() throws Exception {
        String[] args = {"-i", "test.xml"};

        new JCommander(cmdLine, args);

        assertTrue(cmdLine.isImportEnabled());
        assertEquals(1, cmdLine.getFiles().size());
    }

    @Test
    @Ignore("not complete")
    public void testFilterParameter() {
        String[] args = {"-F"};

        new JCommander(cmdLine, args);

        // TODO assertTrue(cmdLine.isFilter());
    }

    public void testDebugParameter() {
        String[] args = {"--debug"};

        new JCommander(cmdLine, args);

        assertTrue(cmdLine.isDebugEnabled());
    }

    @Test
    public void testSetTempPath() {
        String[] args = {"--temp", "/home/user/temp", "test.xml"};

        new JCommander(cmdLine, args);

        assertEquals("/home/user/temp", cmdLine.getTempPath());
    }

    @Test
    @Ignore("how to specific a windows path?")
    public void testSetTempPathWindows() {
        String[] args = { "--temp", "", "test.xml" };
    }

}
