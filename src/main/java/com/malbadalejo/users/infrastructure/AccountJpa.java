package com.malbadalejo.users.infrastructure;

import com.malbadalejo.users.domain.AccountProvider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
@Getter
@NoArgsConstructor
public class AccountJpa {

    @Id
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    private AccountProvider provider;

    private String externalId;

    private LocalDateTime linkedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserJpa user;

    public AccountJpa(UUID accountId, AccountProvider provider, String externalId, LocalDateTime linkedAt) {
        this.accountId = accountId;
        this.provider = provider;
        this.externalId = externalId;
        this.linkedAt = linkedAt;
    }

    // Setter para mantener la relación bidireccional
    public void setUser(UserJpa user) {
        this.user = user;
    }
}