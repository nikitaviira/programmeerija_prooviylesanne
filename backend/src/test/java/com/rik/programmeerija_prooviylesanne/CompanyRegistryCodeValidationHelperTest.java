package com.rik.programmeerija_prooviylesanne;

import org.junit.jupiter.api.Test;

import static com.rik.programmeerija_prooviylesanne.util.CompanyRegistryCodeValidationHelper.validateCompanyRegistryCode;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompanyRegistryCodeValidationHelperTest {
  @Test
  public void test_valid() {
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
