package com.java.lingvo.validation;

import com.java.lingvo.domain.dto.CardCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ValidFlashcardValidator implements ConstraintValidator<ValidFlashcard, CardCreateRequest> {

    @Override
    public boolean isValid(CardCreateRequest request, ConstraintValidatorContext context) {

        if (request.getTerm() == null || request.getTranslation() == null) {
            return true;
        }


        boolean isSame = request.getTerm().trim().equalsIgnoreCase(request.getTranslation().trim());

        return !isSame;
    }
}