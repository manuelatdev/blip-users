package com.malbadalejo.users.domain;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


@Getter
public class Account {

    private final UUID accountId;
    private final AccountProvider provider; // Ej: GOOGLE, GITHUB, CREDENTIALS
    private final String externalId; // ID proporcionado por el proveedor (ej: Google ID)
    private final LocalDateTime linkedAt;

    public Account(AccountProvider provider, String externalId) {
        this.accountId = UUID.randomUUID();
        this.provider = Objects.requireNonNull(provider, "provider no puede ser null");
        this.externalId = Objects.requireNonNull(externalId, "externalId no puede ser null");
        this.linkedAt = LocalDateTime.now();
    }

    public Account(UUID accountId, AccountProvider provider, String externalId, LocalDateTime linkedAt) {
        this.accountId = Objects.requireNonNull(accountId, "accountId no puede ser null");
        this.provider = Objects.requireNonNull(provider, "provider no puede ser null");
        this.externalId = Objects.requireNonNull(externalId, "externalId no puede ser null");
        this.linkedAt = Objects.requireNonNull(linkedAt, "linkedAt no puede ser null");
    }
}