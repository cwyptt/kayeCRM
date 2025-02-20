package io.github.cwyptt.crm.value;

import io.github.cwyptt.crm.utility.PhoneNumberFormatter;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;


@Embeddable
@NoArgsConstructor
public class PhoneNumber {
    private String number;

    public PhoneNumber(String number) {
        this.number = PhoneNumberFormatter.normalizePhoneNumber(number);
    }

    public String getNumber() {
        return PhoneNumberFormatter.formatPhoneNumber(number);
    }

    public void setNumber(String number) {
        this.number = PhoneNumberFormatter.normalizePhoneNumber(number);
    }

    @Override
    public String toString() {
        return getNumber();
    }

    // equals/hashCode for proper value object behavior
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return PhoneNumberFormatter.normalizePhoneNumber(number)
                .equals(PhoneNumberFormatter.normalizePhoneNumber(that.number));
    }

    @Override
    public int hashCode() {
        return PhoneNumberFormatter.normalizePhoneNumber(number).hashCode();
    }
}
