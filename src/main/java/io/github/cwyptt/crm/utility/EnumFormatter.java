package io.github.cwyptt.crm.utility;

import io.github.cwyptt.crm.enums.CustomerStatus;

public class EnumFormatter {
    public static String format(Enum<?> enumValue) {
        if (enumValue == null) {
            return "";
        }

        String[] words = enumValue.name().toLowerCase().split("_");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            String word = words[i].toLowerCase();
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(i));
        }
        return result.toString();
    }

    public static String formatStatus(CustomerStatus status) {
        return format(status);
    }
}
