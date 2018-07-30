package com.henrique.exception;

public class FileManipulationException extends Exception {

    public FileManipulationException() {
        super();
    }

    public FileManipulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileManipulationException(String message) {
        super(message);
    }

    public FileManipulationException(Throwable cause) {
        super(cause);
    }

}
