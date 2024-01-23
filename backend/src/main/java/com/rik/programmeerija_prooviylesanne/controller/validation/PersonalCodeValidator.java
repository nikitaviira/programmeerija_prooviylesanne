package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.rik.programmeerija_prooviylesanne.util.PersonalCodeValidationHelper.validatePersonalCode;

class PersonalCodeValidator implements ConstraintValidator<PersonalCode, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return validatePersonalCode(value);
  }
}
