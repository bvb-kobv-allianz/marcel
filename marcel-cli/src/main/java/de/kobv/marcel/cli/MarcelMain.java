package de.kobv.marcel.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import de.kobv.marcel.db.mysql.DBMethods;
import de.kobv.marcel.main.Main;
import de.kobv.marcel.parser.MarcXMLParserException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Main class of MARCel command line application.
 *
 * Creates application context and executes commando.
 *
 * TODO LogUtil.logTime("Finished" , " Wrote totally " + Variables.writtenNumRecords + " of "
 *      + Variables.totalNumRecords + " records\n");
 */
public class MarcelMain {

    /**
     * Logger for this class.
     */
    private static final Logger LOG = Logger.getLogger(MarcelMain.class);

    public void run(final String[] args) throws IOException, ClassNotFoundException, SQLException,
            MarcXMLParserException {
        ApplicationContext context = new ClassPathXmlApplicationContext("de/kobv/marcel/cli/spring-marcel.xml");

        MarcelCmdLine cmdLine = context.getBean("cmdLine", MarcelCmdLine.class);

        JCommander commander = null;

        try {
            commander = new JCommander(cmdLine, args);
        }
        catch (ParameterException pe) {
            System.out.println(pe.getMessage());
            System.out.println("Type 'marcel --help' for usage.");
            return;
        }

        if (cmdLine.isShowHelp()) {
            commander.setProgramName("marcel");
            commander.usage();
            if (cmdLine.isDebugEnabled()) {
                cmdLine.printValues();
            }
            return;
        }

        if (cmdLine.isShowVersion()) {
            ResourceBundle bundle = ResourceBundle.getBundle("de/kobv/marcel/cli/marcel-cli");
            System.out.println("MARCel v" + bundle.getString("version")); // TODO show complete header
            return;
        }

        if (cmdLine.isDebugEnabled()) {
            enableDebugLogging();
        }

        Main processor = context.getBean("main", Main.class);

        if (StringUtils.isNotBlank(cmdLine.getDatabaseName())) {
            final String dbName = cmdLine.getDatabaseName();
            ((DBMethods) processor.getDatabaseImport().getDbMethods()).setDatabase(dbName);
        }
        System.out.println("Use database " + ((DBMethods) processor.getDatabaseImport().getDbMethods()).getDatabase());


        long startTime = System.currentTimeMillis();

        try {
            processor.run();
        }
        catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            return;
        }

        System.out.println("Processed [TODO] records in "
                + DurationFormatUtils.formatDuration(System.currentTimeMillis() - startTime, "HH:mm:ss,SSS"));
    }

    /**
     * Setzt den Log Level für die MARCel Packages auf DEBUG.
     */
    public void enableDebugLogging() {
        Logger logger = Logger.getLogger("de.kobv.marcel");
        logger.setLevel(Level.DEBUG);
        enableVerboseLogging();
    }

    /**
     * Konfiguriert detailiertere Ausgaben beim Logging.
     *
     * TODO Pattern als Konstante/Konfigurierbar
     */
    public void enableVerboseLogging() {
        Logger logger = Logger.getLogger("de.kobv.marcel");
        Appender appender = logger.getAppender("CONSOLE");
        Layout layout = appender.getLayout();
        if (layout instanceof PatternLayout) {
            ((PatternLayout) layout).setConversionPattern("[%t] %r %d{ISO8601} %-5p %L %-20c{2} - %m %n");
        }
        else {
            LOG.warn("Layout not PatternLayout: Couldn't configure verbose logging.");
        }
    }

    /**
     * Executes application.
     * @param args Command line parameters
     * TODO review exception handling
     */
    public static void main(final String[] args) throws IOException, ClassNotFoundException, SQLException,
            MarcXMLParserException {
        MarcelMain app = new MarcelMain();
        app.run(args);
    }

}
