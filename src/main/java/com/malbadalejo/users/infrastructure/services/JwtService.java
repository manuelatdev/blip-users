package com.malbadalejo.users.infrastructure.services;

import com.malbadalejo.users.domain.UserRole;
import io.jsonwebtoken.Claims;
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

    public String generateToken(UUID userId, String email, String displayName,
                                String profilePictureUrl, UserRole role) {
        return Jwts.builder()
                .subject(userId.toString())
                .claim("email", email)
                .claim("displayName", displayName)
                .claim("profilePictureUrl", profilePictureUrl)
                .claim("role", role.name())  // Añadimos el rol como claim
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(new SecretKeySpec(secret.getBytes(), "HmacSHA256"))
                .compact();
    }

    // Método adicional para validar y extraer claims del token
    public Claims validateToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Métodos útiles para extraer información específica
    public String getUserIdFromToken(String token) {
        return validateToken(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return validateToken(token).get("role", String.class);
    }

    public String getEmailFromToken(String token) {
        return validateToken(token).get("email", String.class);
    }
}