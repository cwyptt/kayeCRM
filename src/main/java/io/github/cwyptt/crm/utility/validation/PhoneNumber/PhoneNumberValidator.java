package io.github.cwyptt.crm.utility.validation.PhoneNumber;

import io.github.cwyptt.crm.utility.PhoneNumberFormatter;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

//    @Override
//    public void initialize(ValidPhoneNumber constraintAnnotation) {
//
//    }

//    @Override
//    public boolean isValid(String phone, ConstraintValidatorContext context) {
//        return phone.matches(PHONE_NUMBER_REGEX);
//    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        String digits = PhoneNumberFormatter.normalizePhoneNumber(value);
        return digits.length() == 10;
    }
}
