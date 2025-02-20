package io.github.cwyptt.crm.entity;

import io.github.cwyptt.crm.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Core relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private Contact contact;  // The person who is the customer

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;  // Optional - if this customer represents a company relationship

    // These 2 for convenience in UI
    private String contactFullName;
    private String companyName;

    // Customer-specific fields
    @Column(nullable = false)
    private CustomerStatus status;

    @Column(name = "customer_since", nullable = false)
    private LocalDateTime customerSince;

    private String notes;

    // Billing/Account fields
    private String accountNumber;
    private String paymentTerms;
    private String billingPreferences;

    // Audit fields
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
