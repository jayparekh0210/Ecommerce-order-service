package com.ecom.ecomorder.exceptions;

public class RequestNotCompletedException extends RuntimeException {
    public RequestNotCompletedException(String message) {
        super(message);
    }
}
