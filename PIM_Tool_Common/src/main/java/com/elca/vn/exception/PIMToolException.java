package com.elca.vn.exception;

/**
 * Wrapper exception when error occurred in server side
 */
public class PIMToolException extends RuntimeException {

    public PIMToolException(String errorMessage) {
        super(errorMessage);
    }

    public PIMToolException(Throwable t) {
        super(t);
    }

    public PIMToolException(String errorMessage, Throwable t) {
        super(errorMessage, t);
    }
}
