package de.kobv.marcel.core;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 */
public class KobvLibrariesGbvMatcherTest {

    private KobvLibrariesGbvMatcher filter;

    @Before
    public void setUp() {
        this.filter = new KobvLibrariesGbvMatcher();
    }

    @Test
    public void testKobvLibraryTrue() {
        Record record = new Record("b3kat");

        Datafield datafield = new Datafield();
        datafield.setTag("980");

        Subfield subfield = new Subfield();
        subfield.setCode("2");
        subfield.setValue("375");

        datafield.addSubfield(subfield);
        record.addDatafield(datafield);

        assertTrue(filter.matches(record));
    }

    @Test
    public void testKobvLibraryFalse() {
        Record record = new Record("b3kat");

        Datafield datafield = new Datafield();
        datafield.setTag("980");

        Subfield subfield = new Subfield();
        subfield.setCode("2");
        subfield.setValue("400");

        datafield.addSubfield(subfield);
        record.addDatafield(datafield);

        assertFalse(filter.matches(record));
    }

}
