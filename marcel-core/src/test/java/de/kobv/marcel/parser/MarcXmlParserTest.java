package de.kobv.marcel.parser;

import static org.junit.Assert.*;

import de.kobv.marcel.beans.Controlfield;
import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by jens on 2/20/14.
 *
 * TODO weiter ausbauen
 */
public class MarcXmlParserTest {

    private MarcXMLParser parser;

    @Before
    public void setUp() {
        parser = new MarcXMLParser();
    }

    @Test
    public void testParsing() throws Exception {
        InputStream in = getClass().getClassLoader().getResourceAsStream("testrecord.xml");

        assertNotNull(in);

        TestHandler handler = new TestHandler();
        parser.setHandler(handler);
        parser.parse(in);

        Record record = handler.getRecord();

        assertNotNull(record);

        assertEquals("BV037103607", record.getUid());

        List<Controlfield> controlfields = record.getControlfields();

        assertEquals(3, controlfields.size());

        List<Datafield> datafields = record.getDatafields();

        assertEquals(11, datafields.size());
    }

    @Test
    public void testSetGetDataSourceName() {
        assertNull(parser.getDataSourceName());
        parser.setDataSourceName("b3kat");
        assertEquals("b3kat", parser.getDataSourceName());
        parser.setDataSourceName(null);
        assertNull(parser.getDataSourceName());
    }

    @Test
    public void testSetGetHandler() {
        assertNull(parser.getHandler());
        IMarcParserHandler handler = new DefaultMarcParserHandler();
        parser.setHandler(handler);
        assertEquals(handler, parser.getHandler());
        parser.setHandler(null);
        assertNull(parser.getHandler());
    }

    class TestHandler implements IMarcParserHandler {

        private Record record;

        @Override
        public void init() {
        }

        @Override
        public void processRecord(Record record) throws IOException {
            this.record = record;
        }

        @Override
        public void parsingFinished() throws IOException {
        }

        public Record getRecord() {
            return record;
        }

    }

}
