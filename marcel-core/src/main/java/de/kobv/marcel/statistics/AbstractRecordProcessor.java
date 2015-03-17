package de.kobv.marcel.statistics;

/**
 * Created with IntelliJ IDEA.
 * User: jens
 * Date: 3/21/14
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractRecordProcessor implements IMarcRecordProcessor {

    private ErrorHandler errorHandler;

    public void init() {
    }

    public void finish() {
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(final ErrorHandler handler) {
        this.errorHandler = handler;
    }

}
