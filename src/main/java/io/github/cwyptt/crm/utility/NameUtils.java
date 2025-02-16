package io.github.cwyptt.crm.utility;

import org.springframework.util.ReflectionUtils;

/**
 * Utility class for handling name-related operations.
 * Uses reflection to work with any object that has firstName and lastName fields.
 */
public class NameUtils {

    /**
     * Generates a full name from any object containing firstName and lastName fields.
     * Uses reflection to safely access these fields regardless of the object type.
     * Returns an empty string if the fields don't exist or are inaccessible.
     */
    public static String getFullName(Object obj) {
        if (obj == null) {
            return "";
        }

        String firstName = "";
        String lastName = "";

        // Safely get firstName using reflection
        java.lang.reflect.Field firstNameField = ReflectionUtils.findField(obj.getClass(), "firstName");
        if (firstNameField != null) {
            ReflectionUtils.makeAccessible(firstNameField);
            Object firstNameValue = ReflectionUtils.getField(firstNameField, obj);
            if (firstNameValue != null) {
                firstName = firstNameValue.toString().trim();
            }
        }

        // Safely get lastName using reflection
        java.lang.reflect.Field lastNameField = ReflectionUtils.findField(obj.getClass(), "lastName");
        if (lastNameField != null) {
            ReflectionUtils.makeAccessible(lastNameField);
            Object lastNameValue = ReflectionUtils.getField(lastNameField, obj);
            if (lastNameValue != null) {
                lastName = lastNameValue.toString().trim();
            }
        }

        // Combine names with proper spacing
        StringBuilder fullName = new StringBuilder();

        if (!firstName.isEmpty()) {
            fullName.append(firstName);
        }

        if (!lastName.isEmpty()) {
            if (!fullName.isEmpty()) {
                fullName.append(" ");
            }
            fullName.append(lastName);
        }

        return fullName.toString();
    }


}