package de.kobv.marcel.statistics;

import de.kobv.marcel.beans.Record;

/**
 * Created by jens on 2/20/14.
 */
public interface IMarcRecordProcessor {

    void init();

    void process(Record record);

    void finish();

}
