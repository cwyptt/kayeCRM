package io.github.cwyptt.crm.dto;

import io.github.cwyptt.crm.utility.NameUtils;
import io.github.cwyptt.crm.utility.PhoneNumberFormatter;
import io.github.cwyptt.crm.utility.validation.PhoneNumber.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

import static io.github.cwyptt.crm.utility.constant.ValidationConstants.*;
import static io.github.cwyptt.crm.utility.constant.ValidationConstants.EMAIL_REQUIRED;

@Data
public class ContactDto {
    private Long id;

    @NotBlank(message = FIRST_NAME_REQUIRED)
    private String firstName;

    @NotBlank(message = LAST_NAME_REQUIRED)
    private String lastName;

    @Email(message = EMAIL_VALID)
    @NotBlank(message = EMAIL_REQUIRED)
    private String email;

    @ValidPhoneNumber
    private String phone;

    private String position;
    private String department;
    private Long customerId;
    private String customerCompany;
    private boolean isPrimary;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setPhone(String phone) {
        this.phone = PhoneNumberFormatter.formatPhoneNumber(phone);
    }

    public String getFullName() {
        return NameUtils.getFullName(this);
    }

    public boolean isPrimary() {
        return isPrimary;
    }
}
