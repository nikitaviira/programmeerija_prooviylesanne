package com.rik.programmeerija_prooviylesanne.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.lang.Character.getNumericValue;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

public class PersonalCodeValidationHelper {
    private static final DateTimeFormatter DATE_FORMATTER = ofPattern("uuuuMMdd");

    public static boolean validatePersonalCode(String personalCode) {
        if (personalCode == null || personalCode.length() != 11) {
            return false;
        }

        if (isNull(parseDateOfBirth(personalCode))) {
            return false;
        }

        return calculateControlNumber(personalCode) == getNumericValue(personalCode.charAt(personalCode.length() - 1));
    }

    private static int calculateControlNumber(String personalCode) {
        String[] numberArray = personalCode.substring(0, 10).split("");
        List<Integer> numberList = stream(numberArray).map(Integer::valueOf).toList();
        int sum = 0;
        int[] multipliers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 1};

        for (int i = 0; i < numberList.size(); i++) {
            sum += numberList.get(i) * multipliers[i];
        }

        int parsedControlNumber = sum % 11;

        if (parsedControlNumber == 10) {
            sum = 0;
            multipliers = new int[]{3, 4, 5, 6, 7, 8, 9, 1, 2, 3};

            for (int i = 0; i < numberList.size(); i++) {
                sum += numberList.get(i) * multipliers[i];
            }

            parsedControlNumber = sum % 11;

            if (parsedControlNumber == 10) {
                return 0;
            }
        }

        return parsedControlNumber;
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
