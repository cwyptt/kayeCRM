package io.github.cwyptt.crm.utility.validation.PhoneNumber;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.github.cwyptt.crm.utility.constant.ValidationConstants.PHONE_NUMBER_VALID;

@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhoneNumber {
    String message() default PHONE_NUMBER_VALID;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
