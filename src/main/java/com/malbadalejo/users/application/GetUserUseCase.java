package com.malbadalejo.users.application;

import com.malbadalejo.users.domain.User;
import com.malbadalejo.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {
    private final UserRepository userRepository;

    public Optional<User> execute(String email) {
        return userRepository.findByEmail(email);
    }
}