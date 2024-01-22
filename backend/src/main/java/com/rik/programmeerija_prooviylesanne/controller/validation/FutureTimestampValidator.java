package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.nowLocalDateTime;

class FutureTimestampValidator implements ConstraintValidator<FutureTimestamp, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        return value != null && value.isAfter(nowLocalDateTime());
    }
}