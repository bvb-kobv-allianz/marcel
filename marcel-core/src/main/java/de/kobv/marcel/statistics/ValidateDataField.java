package de.kobv.marcel.statistics;

import de.kobv.marcel.beans.Datafield;
import de.kobv.marcel.beans.Record;
import de.kobv.marcel.beans.Subfield;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jens on 2/21/14.
 *
 * TODO "The datafield tag 'tag' is not valid"
 * TODO "The subfield code 'code' in datafield 'tag' is not valid"
 */
public class ValidateDataField extends AbstractRecordProcessor {

    /**
     * Regular expression for validating MARC record data field tags.
     */
    private String dataFieldTagRegExp =
            "(0([1-9A-Z][0-9A-Z])|0([1-9a-z][0-9a-z]))|(([1-9A-Z][0-9A-Z]{2})|([1-9a-z][0-9a-z]{2}))";

    private Map<String, List<String>> nonValidRecords = new HashMap<String, List<String>>();

    @Override
    public void process(final Record record) {
        for (Datafield datafield : record.getDatafields()) {
            validateDatafieldTag(record.getUid(), datafield.getTag());

            for (Subfield subfield : datafield.getSubfields()) {
                validateSubfieldCode(record.getUid(), datafield.getTag(), subfield.getCode());
            }
        }
    }

    private void validateDatafieldTag(final String uid, final String tag) {
        if (!tag.matches(getDataFieldTagRegExp())) {
            getErrorHandler().error(uid, "INVALID_TAG", tag);
        }
    }

    /**
     * @param uid Record identifier
     * @param tag Datafield tag
     * @param code Subfield code
     */
    private void validateSubfieldCode(final String uid, final String tag, final String code) {
        if (!Character.isLetterOrDigit(code.charAt(0))) {
            getErrorHandler().error(uid, "INVALID_SUBFIELD", tag + ":" + code);
        }
    }

    /* TODO not used right now
    private void addValidationMessage(final String uid, final String tag) {
        List<String> errors = nonValidRecords.get(uid);
        if (errors == null) {
            errors = new ArrayList<String>();
            nonValidRecords.put(uid, errors);
        }
        errors.add(tag);
    }*/

    public String getDataFieldTagRegExp() {
        return dataFieldTagRegExp;
    }

    public void setDataFieldTagRegExp(final String regExp) {
        this.dataFieldTagRegExp = regExp;
    }

    public Map<String, List<String>> getNonValidRecords() {
        return nonValidRecords;
    }

}
