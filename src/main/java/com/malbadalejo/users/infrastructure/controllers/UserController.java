package com.malbadalejo.users.infrastructure.controllers;

import com.malbadalejo.users.application.CreateUserUseCase;
import com.malbadalejo.users.application.GetUserByIdUseCase;
import com.malbadalejo.users.application.GetUserUseCase;
import com.malbadalejo.users.domain.Account;
import com.malbadalejo.users.domain.AccountProvider;
import com.malbadalejo.users.domain.User;
import com.malbadalejo.users.infrastructure.dto.GoogleAuthRequest;
import com.malbadalejo.users.infrastructure.dto.UserResponse;
import com.malbadalejo.users.infrastructure.services.GoogleTokenInfo;
import com.malbadalejo.users.infrastructure.services.GoogleTokenVerifierService;
import com.malbadalejo.users.infrastructure.services.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final GetUserUseCase getUserUseCase;
    private final GetUserByIdUseCase getUserByIdUseCase;
    private final CreateUserUseCase createUserUseCase;
    private final GoogleTokenVerifierService googleTokenVerifierService;
    private final JwtService jwtService;

    @PostMapping("/google")
    public ResponseEntity<Map<String, Object>> authenticateWithGoogle(@Valid @RequestBody GoogleAuthRequest request) {
        try {
            // Validar el idToken de Google y obtener email y displayName
            GoogleTokenInfo tokenInfo = googleTokenVerifierService.verify(request.getIdToken());
            String email = tokenInfo.getEmail();
            String displayName = tokenInfo.getDisplayName();
            String profilePictureUrl = tokenInfo.getProfilePictureUrl();
            System.out.println("Email verified: " + email);

            log.info("authenticateWithGoogle");

            // Buscar o crear el usuario
            User user = getUserUseCase.execute(email)
                    .orElseGet(() -> {
                        log.info("authenticateWithGoogle.getUserUseCase.execute");
                        return createUserUseCase.execute(email, displayName, profilePictureUrl, new Account(AccountProvider.GOOGLE, request.getExternalId()));
                    });

            String jwt = jwtService.generateToken(
                    user.getUserId(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getProfilePictureUrl(),
                    user.getRole()
            );
            UserResponse userResponse = new UserResponse(user.getUserId(), user.getEmail(), user.getDisplayName(), user.getProfilePictureUrl(), user.getRole());

            Map<String, Object> response = new HashMap<>();
            response.put("user", userResponse);
            response.put("token", jwt);

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            System.err.println("Verification failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUserById(
            @PathVariable("userId") String userId,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Verificar el token JWT en el encabezado Authorization
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                log.warn("Authorization header missing or invalid");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            String token = authorizationHeader.substring(7); // Quitar "Bearer " del encabezado
            String tokenUserId = jwtService.getUserIdFromToken(token);

            // Asegurarse de que el userId del token coincida con el userId solicitado
            if (!tokenUserId.equals(userId)) {
                log.warn("User ID mismatch: tokenUserId={}, requestedUserId={}", tokenUserId, userId);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            // Buscar el usuario por ID
            User user = getUserByIdUseCase.execute(UUID.fromString(userId))
                    .orElse(null);

            if (user == null) {
                log.warn("User not found: userId={}", userId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            UserResponse userResponse = new UserResponse(
                    user.getUserId(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getProfilePictureUrl(),
                    user.getRole()
            );
            log.info("User retrieved successfully: userId={}", userId);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            log.error("Error retrieving user: userId={}, error={}", userId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}