package com.grasset.exception;

public class NoSuchModuleException extends Exception {

    public NoSuchModuleException() {
        super();
    }

    public NoSuchModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchModuleException(String message) {
        super(message);
    }

    public NoSuchModuleException(Throwable cause) {
        super(cause);
    }

}
