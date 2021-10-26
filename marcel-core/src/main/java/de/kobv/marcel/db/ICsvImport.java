package de.kobv.marcel.db;

import java.nio.file.Path;

/**
 * Created by jens on 4/8/14.
 */
public interface ICsvImport {

    void setEncoding(String encoding);

    void setPath(Path path);

    void importFile();

}
