package com.rik.programmeerija_prooviylesanne;

import org.junit.jupiter.api.Test;

import static com.rik.programmeerija_prooviylesanne.util.PersonalCodeValidationHelper.validatePersonalCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonalCodeValidationHelperTest {
    @Test
    public void test_valid() {
        String validPersonalCode = "39807093721";
        assertTrue(validatePersonalCode(validPersonalCode));
    }

    @Test
    public void test_tooShort() {
        String validPersonalCode = "3980709372";
        assertFalse(validatePersonalCode(validPersonalCode));
    }

    @Test
    public void test_invalid() {
        String invalidPersonalCode = "12345678901";
        assertFalse(validatePersonalCode(invalidPersonalCode));
    }

    @Test
    public void test_invalidFormat() {
        String invalidFormatPersonalCode = "ABCDEF12345";
        assertFalse(validatePersonalCode(invalidFormatPersonalCode));
    }

    @Test
    public void test_invalidBirthdate() {
        String invalidBirthdatePersonalCode = "99001310965";
        assertFalse(validatePersonalCode(invalidBirthdatePersonalCode));
    }

    @Test
    public void test_invalidCheckDigit() {
        String invalidCheckDigitPersonalCode = "49002010964";
        assertFalse(validatePersonalCode(invalidCheckDigitPersonalCode));
    }
}
