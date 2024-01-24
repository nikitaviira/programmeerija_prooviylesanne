package com.rik.programmeerija_prooviylesanne;

import org.junit.jupiter.api.Test;

import static com.rik.programmeerija_prooviylesanne.util.CompanyRegistryCodeValidationHelper.validateCompanyRegistryCode;
import static com.rik.programmeerija_prooviylesanne.util.PersonalCodeValidationHelper.validatePersonalCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilTest {
    @Test
    public void validatePersonalCodeTest() {
        assertTrue(validatePersonalCode("39807093721"));
        assertFalse(validatePersonalCode("3980709372"));
        assertFalse(validatePersonalCode("12345678901"));
        assertFalse(validatePersonalCode("ABCDEF12345"));
        assertFalse(validatePersonalCode("99001310965"));
        assertFalse(validatePersonalCode("49002010964"));
    }

    @Test
    public void validateCompanyRegistryCodeTest() {
        assertTrue(validateCompanyRegistryCode("12213008"));
        assertTrue(validateCompanyRegistryCode("10000062"));
        assertTrue(validateCompanyRegistryCode("10000640"));
        assertTrue(validateCompanyRegistryCode("14812701"));
        assertTrue(validateCompanyRegistryCode("14420450"));
        assertFalse(validateCompanyRegistryCode("ABCDEFGH"));
        assertFalse(validateCompanyRegistryCode("12213007"));
        assertFalse(validateCompanyRegistryCode("47101010033"));
    }
}
