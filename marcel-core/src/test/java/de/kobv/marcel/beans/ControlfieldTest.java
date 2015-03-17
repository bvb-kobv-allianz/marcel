package de.kobv.marcel.beans;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: jens
 * Date: 3/13/14
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class ControlfieldTest {

    private Controlfield field;

    @Before
    public void setUp() {
        this.field = new Controlfield();
    }

    @Test
    public void testToString() {
        this.field.setTag("001");
        this.field.setValue("BV037157012");

        assertTrue(this.field.toString().startsWith("de.kobv.marcel.beans.Controlfield@"));
        assertTrue(this.field.toString().endsWith("[tag=001, value=BV037157012]"));
    }

}
