package io.github.cwyptt.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "activities")
public class Activity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;  // e.g., "created", "updated", "deleted"

    @Column(nullable = false)
    private String entityType;  // e.g., "Customer", "Contact", "Company"

    private Long entityId;  // ID of the affected entity

    @Column(nullable = false)
    private String description;  // e.g., "Created new customer John Doe"

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
