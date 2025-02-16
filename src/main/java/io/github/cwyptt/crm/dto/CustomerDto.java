package io.github.cwyptt.crm.dto;

import io.github.cwyptt.crm.utility.NameUtils;
import io.github.cwyptt.crm.utility.PhoneNumberFormatter;
import io.github.cwyptt.crm.utility.validation.PhoneNumber.ValidPhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

import static io.github.cwyptt.crm.utility.constant.ValidationConstants.*;

@Data
public class CustomerDto {
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

    private String company;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setPhone(String phone) {
        this.phone = PhoneNumberFormatter.formatPhoneNumber(phone);
    }

    public String getFullName() {
        return NameUtils.getFullName(this);
    }
}
