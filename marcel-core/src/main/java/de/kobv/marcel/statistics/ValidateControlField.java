package de.kobv.marcel.statistics;

import de.kobv.marcel.beans.Controlfield;
import de.kobv.marcel.beans.Record;

import java.util.HashMap;
import java.util.Map;

/**
 * Validates MARC record control field tags.
 *
 * TODO "The controlfield tag '...' is not valid"
 */
public class ValidateControlField extends AbstractRecordProcessor {

    /**
     * Regular expression for validating a control field of a MARC record.
     */
    private String controlFieldTagRegExp = "00[1-9A-Za-z]";

    /**
     * TODO multiple invalid control fields for one record
     */
    private Map<String, String> invalidControlFields = new HashMap<String, String>();

    @Override
    public void process(final Record record) {
        for (Controlfield controlField : record.getControlfields()) {
            String tag = controlField.getTag();
            if (!tag.matches(getControlFieldTagRegExp())) {
                invalidControlFields.put(record.getUid(), tag);
            }
        }
    }

    public String getControlFieldTagRegExp() {
        return controlFieldTagRegExp;
    }

    public void setControlFieldTagRegExp(final String regExp) {
        this.controlFieldTagRegExp = regExp;
    }

}
