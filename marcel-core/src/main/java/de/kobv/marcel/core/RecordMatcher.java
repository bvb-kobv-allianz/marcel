package de.kobv.marcel.core;

import de.kobv.marcel.beans.Record;

/**
 * Interface for classes that check if a MARC record matches required criteria.
 */
public interface RecordMatcher {

    /**
     *
     * @param record MARC record
     * @return true - if record matches criteria; false - if not
     */
    boolean matches(Record record);

}
