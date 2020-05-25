package com.elca.vn.exception;

public class ProjectExecuteException extends RuntimeException {

    public ProjectExecuteException(String errorMessage) {
        super(errorMessage);
    }

    public ProjectExecuteException(Throwable t) {
        super(t);
    }

    public ProjectExecuteException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
}
