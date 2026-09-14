package com.java.lingvo.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(Long id) {
        super("Card not found: " + id);
    }
}
