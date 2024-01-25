package com.rik.programmeerija_prooviylesanne.controller.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

import static com.rik.programmeerija_prooviylesanne.util.DateUtil.nowLocalDateTime;

class FutureTimestampValidator implements ConstraintValidator<FutureTimestamp, LocalDateTime> {
    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (activeProfile.contains("uitest")) {
            return true;
        }
        return value != null && value.isAfter(nowLocalDateTime());
    }
}