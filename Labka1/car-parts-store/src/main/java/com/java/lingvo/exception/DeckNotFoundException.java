package com.java.lingvo.exception;

public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(Integer id) {
        super("Order not found: " + id);
    }
}
