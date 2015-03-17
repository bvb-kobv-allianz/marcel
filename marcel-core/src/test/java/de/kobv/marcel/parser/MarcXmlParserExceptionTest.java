package de.kobv.marcel.parser;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by jens on 2/20/14.
 */
public class MarcXmlParserExceptionTest {

    @Test
    public void testContruct() {
        MarcXMLParserException exception = new MarcXMLParserException("testuid", new NullPointerException("message"));

        assertEquals("testuid", exception.getRecordUid());
        assertTrue(exception.getCause() instanceof NullPointerException);
        assertEquals("message", exception.getCause().getMessage());
    }

}
