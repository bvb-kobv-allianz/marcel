package de.kobv.marcel.parser;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jens on 2/20/14.
 */
public interface IMarcParser {

    void parse(InputStream inputStream) throws IOException, MarcXMLParserException;

}
