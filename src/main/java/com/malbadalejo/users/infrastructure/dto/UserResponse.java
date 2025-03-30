package com.malbadalejo.users.infrastructure.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class UserResponse {
    private final UUID userId;
    private final String email;
    private final String displayName;
    private final String profilePictureUrl;
}