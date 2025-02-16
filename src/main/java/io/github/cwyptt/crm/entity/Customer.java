package io.github.cwyptt.crm.entity;

import io.github.cwyptt.crm.utility.NameUtils;
import io.github.cwyptt.crm.utility.PhoneNumberFormatter;
import io.github.cwyptt.crm.utility.validation.PhoneNumber.ValidPhoneNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static io.github.cwyptt.crm.utility.constant.ValidationConstants.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = FIRST_NAME_REQUIRED)
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = LAST_NAME_REQUIRED)
    @Column(name = "last_name")
    private String lastName;

    @Email(message = EMAIL_VALID)
    @NotBlank(message = EMAIL_REQUIRED)
    @Column(unique = true)
    private String email;

    @ValidPhoneNumber
    private String phone;

    private String company;

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

    public void setPhone(String phone) {
        this.phone = PhoneNumberFormatter.formatPhoneNumber(phone);
    }
}
