package com.server.db.service;

import com.server.db.domain.User;
import com.server.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Async
    public CompletableFuture<List<User>> findAll() {
        return CompletableFuture.completedFuture(userRepository.findAllByOrderByCreationTimeDesc());
    }

    @Async
    public CompletableFuture<User> findByLoginAndPassword(final String login, final String password) {
        return CompletableFuture.completedFuture(login == null || password == null ? null
                : userRepository.findByLoginAndPassword(login, password));
    }

    public User findById(final long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Async
    public void makeAdmin(final User user) {
        user.setAdmin(true);
        userRepository.save(user);
    }

    @Async
    public void downgrade(final User user) {
        user.setAdmin(false);
        userRepository.save(user);
    }

    @Async
    public CompletableFuture<User> save(final User user) {
        return CompletableFuture.completedFuture(userRepository.save(user));
    }

    @Async
    public void updateLogin(final User user, final String password, final String newLogin) {
        userRepository.updateLogin(user.getId(), user.getLogin(), password, newLogin);
    }

    @Async
    public void updateName(final User user, final String password, final String newName) {
        userRepository.updateName(user.getId(), user.getLogin(), password, newName);
    }

    @Async
    public void updatePasswordSha(final User user, final String password) {
        userRepository.updatePasswordSha(user.getId(), user.getLogin(), password);
    }

    public User findByLogin(final String login) {
        return userRepository.findAllByLogin(login);
    }

    @Async
    public CompletableFuture<Long> countAll() {
        return CompletableFuture.completedFuture(userRepository.count());
    }

    @Async
    public void deleteById(final User user) {
        userRepository.deleteById(user.getId());
    }

    public boolean isLoginVacant(final String login) {
        return findByLogin(login) == null;
    }
}
