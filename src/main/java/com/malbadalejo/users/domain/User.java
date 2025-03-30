package com.malbadalejo.users.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
public class User {

    private final UUID userId;
    private final String email;
    private String displayName;
    private String profilePictureUrl;
    private final List<Account> accounts;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public User(String email) {
        this.userId = UUID.randomUUID();
        this.email = Objects.requireNonNull(email, "email no puede ser null");
        this.displayName = null;
        this.profilePictureUrl = null;
        this.accounts = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public User(String email, String displayName, String profilePictureUrl) {
        this.userId = UUID.randomUUID();
        this.email = Objects.requireNonNull(email, "email no puede ser null");
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.accounts = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public User(UUID userId, String email, String displayName, String profilePictureUrl, List<Account> accounts,
                LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.userId = Objects.requireNonNull(userId, "userId no puede ser null");
        this.email = Objects.requireNonNull(email, "email no puede ser null");
        this.displayName = displayName;
        this.profilePictureUrl = profilePictureUrl;
        this.accounts = accounts != null ? new ArrayList<>(accounts) : new ArrayList<>();
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt no puede ser null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "updatedAt no puede ser null");
    }

    public void addAccount(Account account) {
        Objects.requireNonNull(account, "account no puede ser null");
        if (accounts.stream().anyMatch(a -> a.getProvider().equals(account.getProvider()))) {
            throw new IllegalStateException("Ya existe una cuenta con el proveedor: " + account.getProvider());
        }
        accounts.add(account);
    }

    public void updateDisplayName(String newDisplayName) {
        this.displayName = newDisplayName;
        this.updatedAt = LocalDateTime.now(); // Actualiza la fecha de modificación
    }

    public void updateProfilePictureUrl(String newProfilePictureUrl) {
        this.profilePictureUrl = newProfilePictureUrl;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasAccountForProvider(AccountProvider provider) {
        return accounts.stream().anyMatch(a -> a.getProvider().equals(provider));
    }
}