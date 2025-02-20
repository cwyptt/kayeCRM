package io.github.cwyptt.crm.dto;

import io.github.cwyptt.crm.utility.PhoneNumberConverter;
import io.github.cwyptt.crm.value.PhoneNumber;
import jakarta.persistence.Convert;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

import static io.github.cwyptt.crm.utility.constant.ValidationConstants.*;

@Data
public class CompanyDto {
    private Long id;

    @NotBlank(message = COMPANY_NAME_REQUIRED)
    private String name;

    @Email(message = EMAIL_VALID)
    @NotBlank(message = EMAIL_REQUIRED)
    private String email;

    @Convert(converter = PhoneNumberConverter.class)
    private PhoneNumber phone;


    private String website;
    private String industry;
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
