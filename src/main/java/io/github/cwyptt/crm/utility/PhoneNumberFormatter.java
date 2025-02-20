package io.github.cwyptt.crm.utility;

public class PhoneNumberFormatter {

    public static String formatPhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }

        // Remove all non-digit characters
        String digitsOnly = phone.replaceAll("[^0-9]", "");

        if (digitsOnly.length() == 10) {
            return String.format("(%s) %s-%s",
                    digitsOnly.substring(0,3),
                    digitsOnly.substring(3,6),
                    digitsOnly.substring(6));
        }

        return phone;
    }

    public static String normalizePhoneNumber(String phone) {
        if (phone == null) {
            return null;
        }
        return phone.replaceAll("[^0-9]", "");
    }
}
