package com.java.lingvo.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handlesIllegalArgumentException_asBadRequest() {
        ResponseEntity<ApplicationErrorResponse> response =
                handler.handle(new IllegalArgumentException("bad input"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("bad input", response.getBody().getMessage());
    }

    @Test
    void handlesAccessDeniedException_asForbidden() {
        ResponseEntity<ApplicationErrorResponse> response =
                handler.handle(new AccessDeniedException("not allowed"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("not allowed", response.getBody().getMessage());
    }

    @Test
    void handlesCardNotFoundException_asNotFound() {
        ResponseEntity<ApplicationErrorResponse> response =
                handler.handle(new CardNotFoundException(42L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Card not found: 42", response.getBody().getMessage());
    }

    @Test
    void handlesDeckNotFoundException_asNotFound() {
        ResponseEntity<ApplicationErrorResponse> response =
                handler.handle(new DeckNotFoundException(7L));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Deck not found: 7", response.getBody().getMessage());
    }
}
