package io.github.cwyptt.crm.dto;

import io.github.cwyptt.crm.enums.CustomerStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDto {
    private Long id;

    private Long contactId;
    private Long companyId;

    // These 2 for convenience in UI
    private String contactFullName;
    private String companyName;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    private LocalDateTime customerSince;

    private String notes;

    private String accountNumber;
    private String paymentTerms;
    private String billingPreferences;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
