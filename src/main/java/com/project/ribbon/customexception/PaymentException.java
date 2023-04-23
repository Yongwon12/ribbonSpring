package com.project.ribbon.customexception;

public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}
