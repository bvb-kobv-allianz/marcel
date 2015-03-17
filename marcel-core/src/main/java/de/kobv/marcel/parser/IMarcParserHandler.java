package de.kobv.marcel.parser;

import de.kobv.marcel.beans.Record;

import java.io.IOException;

/**
 * Created by jens on 2/19/14.
 *
 * TODO review exceptions
 */
public interface IMarcParserHandler {

    void init();

    void processRecord(Record record) throws IOException;

    void parsingFinished() throws IOException;

}
