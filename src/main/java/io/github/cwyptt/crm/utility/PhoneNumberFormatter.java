package io.github.cwyptt.crm.utility;

public class PhoneNumberFormatter {

    public static String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }

        // Remove all non-digit characters
        String digitsOnly = phoneNumber.replaceAll("[^0-9]", "");

        if (digitsOnly.length() == 10) {
            return String.format("(%s) %s-%s",
                    digitsOnly.substring(0,3),
                    digitsOnly.substring(3,6),
                    digitsOnly.substring(6));
        }

        return phoneNumber;
    }

    public static String normalizePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return null;
        }
        return phoneNumber.replaceAll("[^0-9]", "");
    }
}
