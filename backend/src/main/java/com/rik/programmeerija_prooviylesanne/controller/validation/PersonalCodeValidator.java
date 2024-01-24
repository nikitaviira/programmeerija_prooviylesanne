package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.rik.programmeerija_prooviylesanne.util.CodeValidationHelper.validatePersonalCode;

class PersonalCodeValidator implements ConstraintValidator<ValidPersonalCode, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return validatePersonalCode(value);
  }
}
