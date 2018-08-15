package com.grasset.exception;

public class NoSuchMetaFileException extends Exception {

    public NoSuchMetaFileException() {
        super();
    }

    public NoSuchMetaFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchMetaFileException(String message) {
        super(message);
    }

    public NoSuchMetaFileException(Throwable cause) {
        super(cause);
    }

}
