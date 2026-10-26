package org.tcpri.workspace.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class AppUser {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 190)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private Role role = Role.MEMBER;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 20)
    private AccountStatus status = AccountStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected AppUser() {}

    public AppUser(String name, String email, String passwordHash) {
        this.name = name;
        this.email = email.toLowerCase().trim();
        this.passwordHash = passwordHash;
        this.role = Role.MEMBER;
        this.status = AccountStatus.PENDING;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    public AccountStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setRole(Role role) { this.role = role; }
    public void setStatus(AccountStatus status) { this.status = status; }
}
