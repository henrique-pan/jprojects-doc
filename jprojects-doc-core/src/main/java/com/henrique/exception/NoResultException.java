package com.henrique.exception;

public class NoResultException extends Exception {

    public NoResultException() {
        super();
    }

    public NoResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoResultException(String message) {
        super(message);
    }

    public NoResultException(Throwable cause) {
        super(cause);
    }

}
