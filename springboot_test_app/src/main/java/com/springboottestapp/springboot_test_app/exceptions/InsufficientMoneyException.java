package com.springboottestapp.springboot_test_app.exceptions;

public class InsufficientMoneyException extends RuntimeException {

    public InsufficientMoneyException(String message) {
        super(message);
    }
}
