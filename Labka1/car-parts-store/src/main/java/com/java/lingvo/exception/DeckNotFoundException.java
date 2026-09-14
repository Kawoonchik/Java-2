package com.java.lingvo.exception;

public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(Integer id) {
        super("Deck not found: " + id);
    }
}
