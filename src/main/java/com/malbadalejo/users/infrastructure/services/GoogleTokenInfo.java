package com.malbadalejo.users.infrastructure.services;

import lombok.Value;

@Value
public class GoogleTokenInfo {
    String email;
    String displayName;
    String profilePictureUrl;
}