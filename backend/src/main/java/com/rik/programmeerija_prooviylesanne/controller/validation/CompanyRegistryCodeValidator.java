package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import static com.rik.programmeerija_prooviylesanne.util.CodeValidationHelper.validateCompanyRegistryCode;


class CompanyRegistryCodeValidator implements ConstraintValidator<CompanyRegistryCode, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return validateCompanyRegistryCode(value);
  }
}
