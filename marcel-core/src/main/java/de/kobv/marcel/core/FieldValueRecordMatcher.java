package de.kobv.marcel.core;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;

import java.util.HashSet;
import java.util.Set;

/**
 */
public class FieldValueRecordMatcher implements RecordMatcher {

    private Set<String> values = new HashSet<String>();

    private String datafieldTag;

    private String subfieldCode;

    @Override
    public boolean matches(final Record record) {
        for (Datafield datafield : record.getDatafields()) {
            if (datafield.getTag().equals(datafieldTag)) {
                for (Subfield subfield : datafield.getSubfields()) {
                    if (subfield.getCode() == subfieldCode) {
                        // TODO setHasLibrary(true);
                        if (values.contains(subfield.getValue())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public Set<String> getValues() {
        return values;
    }

    public void setValues(final Set<String> valuesParam) {
        this.values = valuesParam;
    }

    public String getDatafieldTag() {
        return datafieldTag;
    }

    public void setDatafieldTag(final String dfTag) {
        this.datafieldTag = dfTag;
    }

    public String getSubfieldCode() {
        return subfieldCode;
    }

    public void setSubfieldCode(final String code) {
        this.subfieldCode = code;
    }

}
