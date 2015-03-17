package de.kobv.marcel.db;

import java.io.UnsupportedEncodingException;

/**
 * Interface for MARCel database.
 */
public interface IMarcelDatabase {

    void createDatabase();

    void createTables();

    void dropDatabase();

    void dropTables();

    /**
     * TODO think about it!
     */
    void deleteDuplicateRecords() throws UnsupportedEncodingException;

}
