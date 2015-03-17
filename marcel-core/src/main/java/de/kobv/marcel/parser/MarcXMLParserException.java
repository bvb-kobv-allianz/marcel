package de.kobv.marcel.parser;

public class MarcXMLParserException extends Exception {

    private static final long serialVersionUID = -3717230896475276185L;

    private String recordUid;

    public MarcXMLParserException(final String ruid, final Exception exception) {
        super(exception);
        this.recordUid = ruid;
    }

    public String getRecordUid() {
        return this.recordUid;
    }
}
