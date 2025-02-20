package io.github.cwyptt.crm.entity;

import io.github.cwyptt.crm.value.PhoneNumber;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(unique = true)
    private String email;

    @Embedded
    private PhoneNumber phone;

    private String position;
    private String department;

    // Company relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company; // Optional - company the contact works for

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id", nullable = false)
//    private Customer customer;

    @Column(name = "is_primary_contact")
    private boolean isPrimaryContact; // Are they the primary contact for their company?

    @Column(name = "is_customer")
    private boolean isCustomer; // Indicates if this contact is also a customer

    // Customer-specific fields (only relevant if isCustomer is true)
    private LocalDateTime customerSince;  // When did they become a customer?
    private String customerStatus;  // Active, Inactive, Prospect, etc.

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
