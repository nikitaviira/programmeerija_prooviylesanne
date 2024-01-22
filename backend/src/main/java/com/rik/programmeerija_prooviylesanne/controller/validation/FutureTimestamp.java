package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureTimestampValidator.class)
public @interface FutureTimestamp {

    String message() default "Toimumisaeg peab olema tulevikus";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}