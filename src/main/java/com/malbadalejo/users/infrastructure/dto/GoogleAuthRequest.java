package com.malbadalejo.users.infrastructure.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GoogleAuthRequest {
    @NotNull(message = "idToken is required")
    private String idToken;

    @NotNull(message = "externalId is required")
    private String externalId; // El ID único de Google
}