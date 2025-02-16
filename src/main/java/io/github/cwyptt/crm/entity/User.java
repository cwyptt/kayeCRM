//package io.github.cwyptt.crm.entity;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//
//import java.time.LocalDateTime;
//
//import static io.github.cwyptt.crm.utility.constant.ValidationConstants.*;
//
//@Entity
//@Table(name = "users")
//public class User {
//
//    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @NotBlank(message = USERNAME_REQUIRED)
//    @Column(unique = true)
//    private String username;
//
////    @Email(message = EMAIL_VALID)
////    @NotBlank(message = EMAIL_REQUIRED)
////    @Column(unique = true)
////    private String email;
//
//    @NotBlank(message = PASSWORD_REQUIRED)
//    private String password;
//
//    // private String status; // active|inactive|suspended
//    private String role;
//    // private List<Role> roles;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//}
