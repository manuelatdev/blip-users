package com.malbadalejo.users.infrastructure;

import com.malbadalejo.users.domain.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
public class UserJpa {

    @Id
    private UUID userId;

    private String email;

    private String displayName;

    private String profilePictureUrl;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountJpa> accounts = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    public UserJpa(UUID userId, String email, String displayName, String profilePictureUrl,
                   List<AccountJpa> accounts, LocalDateTime createdAt, LocalDateTime updatedAt,
                   UserRole role) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.accounts = accounts != null ? new ArrayList<>(accounts) : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role != null ? role : UserRole.USER;
    }

    // Método auxiliar para agregar una cuenta y mantener la relación bidireccional
    public void addAccount(AccountJpa account) {
        accounts.add(account);
        account.setUser(this);
    }
}