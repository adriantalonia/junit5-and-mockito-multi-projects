package com.junit5app.exceptions;

public class InsufficientMoneyException extends RuntimeException {

    public InsufficientMoneyException(String message) {
        super(message);
    }
}
