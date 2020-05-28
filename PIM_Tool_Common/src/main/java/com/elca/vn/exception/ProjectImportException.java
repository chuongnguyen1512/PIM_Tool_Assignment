package com.elca.vn.exception;

/**
 * Wrapper exception when persisting project data
 */
public class ProjectImportException extends RuntimeException {

    public ProjectImportException(String errorMessage) {
        super(errorMessage);
    }

    public ProjectImportException(Throwable t) {
        super(t);
    }

    public ProjectImportException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
}
