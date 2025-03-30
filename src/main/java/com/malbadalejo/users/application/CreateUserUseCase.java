package com.malbadalejo.users.application;

import com.malbadalejo.users.domain.Account;
import com.malbadalejo.users.domain.User;
import com.malbadalejo.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {
    private final UserRepository userRepository;

    public User execute(String email, String displayName, String profilePictureUrl, Account account) {
        User newUser = new User(email, displayName, profilePictureUrl);
        newUser.addAccount(account);
        return userRepository.save(newUser);
    }
}