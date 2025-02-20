package io.github.cwyptt.crm.utility;

import io.github.cwyptt.crm.value.PhoneNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Converter(autoApply = true)
public class PhoneNumberConverter implements AttributeConverter<PhoneNumber, String> {
    @Override
    public String convertToDatabaseColumn(PhoneNumber phone) {
        return phone == null ? null : phone.getNumber();
    }

    @Override
    public PhoneNumber convertToEntityAttribute(String number) {
        return number == null ? null : new PhoneNumber(number);
    }
}
