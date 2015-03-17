package de.kobv.marcel.core;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Checks if a record belongs to a KOBV library.
 */
public class KobvLibrariesB3katMatcher extends FieldValueRecordMatcher {

    public KobvLibrariesB3katMatcher() {
        String[] values = new String[] {"FUBA1", "DE-188", "FUBA2", "DE-578", "FUBA3", "DE-B16", "HUBA1", "DE-11",
                "HUBA2", "DE-B1564", "HUBA3", "DE-B2096", "HUBA5", "HUBA6", "TUBA1", "DE-83", "UDKA1", "DE-B170",
                "BTU01", "DE-Co1", "EUV01", "DE-521", "ASH01", "DE-1533", "HTW01", "DE-523", "HWR01", "DE-577",
                "DE-B7211", "BFB01", "DE-522", "BFE01", "DE-Eb1", "BFP01", "DE-525", "BHL01", "DE-Sen1", "BTW01",
                "DE-526", "ADK01", "DE-B486", "BHS01", "DE-B768", "BTM01", "DE-B1550", "DHM01", "DE-B496", "FES01",
                "DE-Bo133"
        };

        setDatafieldTag("049");
        setSubfieldCode("a");

        setValues(new HashSet<String>(Arrays.asList(values)));
    }

}
