package com.junit5app.models.exceptions;

public class InsufficientMoneyException extends RuntimeException {

    public InsufficientMoneyException(String message) {
        super(message);
    }
}
