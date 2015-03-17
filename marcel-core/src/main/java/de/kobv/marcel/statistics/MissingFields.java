package de.kobv.marcel.statistics;

import de.kobv.marcel.beans.Record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jens on 2/21/14.
 */
public class MissingFields extends AbstractRecordProcessor {

    /**
     * TODO make configurable
     */
    public static final String[] REQUIRED_FIELDS = {"245a"};

    /**
     * Stores what records have missing fields.
     *
     * Field Identifier => List of Record Identifiers
     */
    private Map<String, List<String>> missingFields = new HashMap<String, List<String>>();

    @Override
    public void process(final Record record) {
        for (String requiredField : REQUIRED_FIELDS) {
            if (!record.containsSubfield(requiredField)) {
                addMissingField(record.getUid(), requiredField);
            }
        }
    }

    private void addMissingField(final String recordUid, final String field) {
        List<String> uids = missingFields.get(field);
        if (uids == null) {
            uids = new ArrayList<String>();
            missingFields.put(field, uids);
        }
        uids.add(recordUid);
    }

    public Map<String, List<String>> getMissingFields() {
        return missingFields;
    }

}
