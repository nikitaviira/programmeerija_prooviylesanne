package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PersonalCodeValidator.class)
public @interface PersonalCode {

  String message() default "Isikukood ei ole valiidne";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}