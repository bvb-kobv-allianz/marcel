package de.kobv.marcel.db.mysql;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by jens on 2/20/14.
 */
public class DbMethodsTest {

    private DBMethods dbMethods;

    @Before
    public void setUp() {
        this.dbMethods = new DBMethods();
    }

    @Test
    public void testGetSql() {
        assertEquals("(?, ?)", dbMethods.getSql("recordPlaceholders"));
    }

    @Test
    public void testGetSqlWithDataSource() {
        dbMethods.setDatabase("B3KAT");
        assertEquals("GRANT SELECT ON B3KAT.* TO 'kobv'@'localhost'", dbMethods.getSql("grantSelect"));
    }

    @Test
    public void testGetCsvDelimiter() {
        assertEquals(",", dbMethods.getCsvDelimiter());
    }

    @Test
    public void testSetCsvDelimiter() {
        dbMethods.setCsvDelimiter(":");
        assertEquals(":", dbMethods.getCsvDelimiter());
    }

}
