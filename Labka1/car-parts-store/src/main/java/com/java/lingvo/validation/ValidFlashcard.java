package com.java.lingvo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE})

@Retention(RetentionPolicy.RUNTIME)

@Constraint(validatedBy = ValidFlashcardValidator.class)
public @interface ValidFlashcard {

    String message() default "Термін та переклад не можуть бути однаковими";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}