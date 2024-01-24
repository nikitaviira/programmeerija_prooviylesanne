package com.rik.programmeerija_prooviylesanne.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static java.lang.Character.getNumericValue;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.stream.IntStream.range;

public class CodeValidationHelper {
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("uuuuMMdd");

    public static boolean validatePersonalCode(String personalCode) {
        return personalCode != null &&
            personalCode.length() == 11 &&
            parseDateOfBirth(personalCode) != null &&
            calculateControlNumber(personalCode) == getNumericValue(personalCode.charAt(10));
    }

    public static boolean validateCompanyRegistryCode(String registryCode) {
        return registryCode != null &&
            registryCode.length() == 8 &&
            registryCode.matches("^[0-9]{8}$") &&
            calculateControlNumber(registryCode) == getNumericValue(registryCode.charAt(registryCode.length() - 1));
    }

    static int calculateControlNumber(String personalCode) {
        int[] w1 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};
        int[] w2 = {3, 4, 5, 6, 7, 8, 9, 1, 2, 3};
        int[] digits = range(0, personalCode.length() - 1)
            .map(i -> getNumericValue(personalCode.charAt(i)))
            .toArray();

        int controlNumber = calculateSum(digits, w1) % 11;
        if (controlNumber == 10) {
            controlNumber = calculateSum(digits, w2) % 11;
            return controlNumber == 10 ? 0 : controlNumber;
        }
        return controlNumber;
    }

    private static int calculateSum(int[] digits, int[] multipliers) {
        return range(0, digits.length)
            .map(i -> digits[i] * multipliers[i])
            .sum();
    }

    private static LocalDate parseDateOfBirth(String personalCode) {
        String dateString = personalCode.substring(1, 7);
        dateString = switch (getGenderIdentifier(personalCode)) {
            case 1, 2 -> "18" + dateString;
            case 3, 4 -> "19" + dateString;
            default -> "20" + dateString;
        };

        try {
            return LocalDate.parse(dateString, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static int getGenderIdentifier(String personalCode) {
        return getNumericValue(personalCode.charAt(0));
    }
}
