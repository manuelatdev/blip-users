package com.malbadalejo.users.application;

import com.malbadalejo.users.domain.User;
import com.malbadalejo.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserByIdUseCase {
    private final UserRepository userRepository;

    public Optional<User> execute(UUID userId) {
        return userRepository.findById(userId);
    }
}