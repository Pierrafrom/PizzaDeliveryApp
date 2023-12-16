package com.pizzadelivery.model;

public class RateLimitExceededException extends Exception{
    public RateLimitExceededException(String message) {
        super(message);
    }
}
