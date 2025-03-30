package com.malbadalejo.users.infrastructure.services;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(UUID userId, String email, String displayName, String profilePictureUrl) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("displayName", displayName)
                .claim("profilePictureUrl", profilePictureUrl)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .compact();
    }
}
