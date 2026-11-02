package com.java.lingvo.exception;

public class DeckNotFoundException extends RuntimeException {
    public DeckNotFoundException(Long id) {
        super("Deck not found: " + id);
    }
}
