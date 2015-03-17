package de.kobv.marcel.core;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class KobvLibrariesGbvMatcher extends FieldValueRecordMatcher {

    public KobvLibrariesGbvMatcher() {
        String[] values = new String[] {
                "179", // Berlin-Brandenburgische Akademie der Wissenschaften
                "285", // UB Potsdam
                "339", // Deutsches Institut für Menschenrechte
                "375", // Deutsches Zentrum für Altersfragen
                "380", // Hertie School of Governance
                "611", // Humboldt-Viadrina School of Governance
                "376", // Katholische Hochschule für Sozialwesen
                "754", // Max-Planck-Institut für Bildungsforschung
                "11", // Staatsbibliothek zu Berlin
                "156", // Ibero-Amerikanisches Institut Preußischer Kulturbesitz
                "393", // Wissenschaftszentrum  Berlin für Sozialforschung
                "181" // Stiftung Preuß.Kulturbesitz Kunstbibliotheken
        };

        setValues(new HashSet<String>(Arrays.asList(values)));

        setDatafieldTag("980");
        setSubfieldCode("2");
    }

}
