package com.example.estacioneschallenge.exception;

public class InvalidGraphOperationException extends Exception {

    public InvalidGraphOperationException() {
        super();
    }

    public InvalidGraphOperationException(String message) {
        super(message);
    }

    public InvalidGraphOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidGraphOperationException(Throwable cause) {
        super(cause);
    }
}