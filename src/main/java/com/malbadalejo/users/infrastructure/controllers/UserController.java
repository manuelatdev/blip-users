package com.malbadalejo.users.infrastructure.controllers;

import com.malbadalejo.users.application.CreateUserUseCase;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final GetUserUseCase getUserUseCase;
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


            // Buscar o crear el usuario
            User user = getUserUseCase.execute(email)
                    .orElseGet(() -> createUserUseCase.execute(email, displayName, profilePictureUrl, new Account(AccountProvider.GOOGLE, request.getExternalId())));

            String jwt = jwtService.generateToken(
                    user.getUserId(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getProfilePictureUrl(),
                    user.getRole()  // Añadir el rol
            );
            UserResponse userResponse = new UserResponse(user.getUserId(), user.getEmail(), user.getDisplayName(), user.getProfilePictureUrl());

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
}