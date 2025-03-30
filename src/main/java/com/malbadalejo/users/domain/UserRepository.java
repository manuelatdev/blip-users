package com.malbadalejo.users.domain;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID userId);
    Optional<User> findByEmail(String email);
}
