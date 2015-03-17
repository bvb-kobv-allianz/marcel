package de.kobv.marcel.core;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class FieldValueRecordMatcherTest {

    private FieldValueRecordMatcher filter;

    private Record record;

    @Before
    public void setUp() {
        this.filter = new FieldValueRecordMatcher();
        this.filter.setDatafieldTag("049");
        this.filter.setSubfieldCode("a");

        Record record = new Record("b3kat");

        Datafield datafield = new Datafield();
        datafield.setTag("049");

        Subfield subfield = new Subfield();
        subfield.setCode("a");
        subfield.setValue("TEST");

        datafield.addSubfield(subfield);
        record.addDatafield(datafield);

        this.record = record;
    }

    @Test
    public void testMatchTrue() {
        String[] values = { "TEST" };

        filter.setValues(new HashSet<String>(Arrays.asList(values)));

        assertTrue(filter.matches(record));
    }

    @Test
    public void testMatchFalse() {
        String[] values = { "TEST1", "TEST2", "ABC" };

        filter.setValues(new HashSet<String>(Arrays.asList(values)));

        assertFalse(filter.matches(record));
    }

}
