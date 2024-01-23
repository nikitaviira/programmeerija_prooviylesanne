package com.rik.programmeerija_prooviylesanne.util;

import static com.rik.programmeerija_prooviylesanne.util.PersonalCodeValidationHelper.calculateControlNumber;
import static java.lang.Character.getNumericValue;

public class CompanyRegistryCodeValidationHelper {
  public static boolean validateCompanyRegistryCode(String registryCode) {
    if (registryCode.length() != 8 || !registryCode.matches("^[0-9]{8}$")) {
      return false;
    }

    return calculateControlNumber(registryCode) == getNumericValue(registryCode.charAt(registryCode.length() - 1));
  }
}
