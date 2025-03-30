package com.malbadalejo.users.infrastructure;

import com.malbadalejo.users.domain.Account;
import com.malbadalejo.users.domain.User;
import com.malbadalejo.users.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserRepositoryJpa jpaUserRepository;

    @Override
    public User save(User user) {
        UserJpa userJpa = toUserJpa(user);
        UserJpa savedUserJpa = jpaUserRepository.save(userJpa);
        return toUser(savedUserJpa);
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return jpaUserRepository.findById(userId).map(this::toUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::toUser);
    }

    // Métodos de mapeo
    private UserJpa toUserJpa(User user) {
        UserJpa userJpa = new UserJpa(
                user.getUserId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getProfilePictureUrl(),
                new ArrayList<>(), // Se llenará con las cuentas a continuación
                user.getCreatedAt(),
                user.getUpdatedAt()
        );

        // Mapear las cuentas
        for (Account account : user.getAccounts()) {
            AccountJpa accountJpa = new AccountJpa(
                    account.getAccountId(),
                    account.getProvider(),
                    account.getExternalId(),
                    account.getLinkedAt()
            );
            userJpa.addAccount(accountJpa); // Mantener la relación bidireccional
        }

        return userJpa;
    }

    private User toUser(UserJpa userJpa) {
        return new User(
                userJpa.getUserId(),
                userJpa.getEmail(),
                userJpa.getDisplayName(),
                userJpa.getProfilePictureUrl(),
                userJpa.getAccounts().stream()
                        .map(this::toAccount)
                        .collect(Collectors.toList()),
                userJpa.getCreatedAt(),
                userJpa.getUpdatedAt()
        );
    }

    private Account toAccount(AccountJpa accountJpa) {
        return new Account(
                accountJpa.getAccountId(),
                accountJpa.getProvider(),
                accountJpa.getExternalId(),
                accountJpa.getLinkedAt()
        );
    }
}