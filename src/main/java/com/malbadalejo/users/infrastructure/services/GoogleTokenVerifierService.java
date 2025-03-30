package com.malbadalejo.users.infrastructure.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleTokenVerifierService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierService(@Value("${google.client-id}") String clientId) {
        HttpTransport transport = Utils.getDefaultTransport();
        JsonFactory jsonFactory = Utils.getDefaultJsonFactory();
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleTokenInfo verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String email = payload.getEmail();
                String displayName = (String) payload.get("name");
                String profilePictureUrl = (String) payload.get("picture");
                System.out.println("Email: " + email + ", DisplayName: " + displayName + ", Picture: " + profilePictureUrl);
                return new GoogleTokenInfo(email, displayName, profilePictureUrl);
            } else {
                throw new IllegalArgumentException("Invalid Google ID token");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to verify Google ID token", e);
        }
    }
}