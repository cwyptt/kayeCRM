package io.github.cwyptt.crm.dto;

import io.github.cwyptt.crm.utility.NameUtils;
import io.github.cwyptt.crm.value.PhoneNumber;
import jakarta.persistence.Embedded;
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

    @Embedded
    private PhoneNumber phone;


    private String position;
    private String department;


    private Long companyId;
    private String companyName;  // For convenience in UI

    private boolean isPrimaryContact;
    private boolean isCustomer;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getFullName() {
        return NameUtils.getFullName(this);
    }
}