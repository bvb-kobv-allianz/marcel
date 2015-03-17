package de.kobv.marcel.core;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 */
public class KobvLibrariesB3katMatcherTest {

    private KobvLibrariesB3katMatcher filter;

    @Before
    public void setUp() {
        this.filter = new KobvLibrariesB3katMatcher();
    }

    @Test
    public void testKobvLibraryTrue() {
        Record record = new Record("b3kat");

        Datafield datafield = new Datafield();
        datafield.setTag("049");

        Subfield subfield = new Subfield();
        subfield.setCode("a");
        subfield.setValue("HUBA1");

        datafield.addSubfield(subfield);
        record.addDatafield(datafield);

        assertTrue(filter.matches(record));
    }

    @Test
    public void testKobvLibraryFalse() {
        Record record = new Record("b3kat");

        Datafield datafield = new Datafield();
        datafield.setTag("049");

        Subfield subfield = new Subfield();
        subfield.setCode("a");
        subfield.setValue("NOTKOBV");

        datafield.addSubfield(subfield);
        record.addDatafield(datafield);

        assertFalse(filter.matches(record));
    }

}
