package com.java.lingvo.validation;

import com.java.lingvo.domain.dto.CardCreateRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ValidFlashcardValidatorTest {

    private ValidFlashcardValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidFlashcardValidator();
    }

    @Test
    void isValid_ShouldReturnTrue_WhenTermAndTranslationAreDifferent() {
        // Arrange
        CardCreateRequest request = new CardCreateRequest("Hello", "Привіт", "Hello world", "English");

        // Act
        boolean result = validator.isValid(request, context);

        // Assert
        assertTrue(result, "Validation should pass when term and translation are different");
    }

    @Test
    void isValid_ShouldReturnTrue_WhenTermOrTranslationIsNull() {
        // Arrange
        CardCreateRequest request1 = new CardCreateRequest(null, "Привіт", "Hello world", "English");
        CardCreateRequest request2 = new CardCreateRequest("Hello", null, "Hello world", "English");

        // Act & Assert
        assertTrue(validator.isValid(request1, context), "Should return true when term is null");
        assertTrue(validator.isValid(request2, context), "Should return true when translation is null");
    }

    @Test
    void isValid_ShouldReturnFalse_WhenTermAndTranslationAreEqualIgnoringCase() {
        // Arrange
        CardCreateRequest request = new CardCreateRequest("Hello", "hello", "Hello world", "English");

        // Act
        boolean result = validator.isValid(request, context);

        // Assert
        assertFalse(result, "Validation should fail when term and translation are equal (ignoring case)");
    }




}
