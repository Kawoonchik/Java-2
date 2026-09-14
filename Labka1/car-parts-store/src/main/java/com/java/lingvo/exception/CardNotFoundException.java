package com.java.lingvo.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Integer id) {
        super("Card not found: " + id);
    }
}
