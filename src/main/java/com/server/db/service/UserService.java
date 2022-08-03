package com.server.db.service;

import com.server.db.annotations.*;
import com.server.db.domain.User;
import com.server.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAllByOrderByCreationTimeDesc();
    }

    @NoOuterAccess
    public User findByLoginAndPassword(final String login, final String password) {
        return login == null || password == null ? null
                : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(final long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Admin
    public void makeAdmin(final User user) {
        user.setAdmin(true);
        save(user);
    }

    @Admin
    public void downgrade(final User user) {
        user.setAdmin(false);
        save(user);
    }

    @SystemOnly
    public User save(final User user) {
        return userRepository.save(user);
    }

    @PrivateOnly
    @Confirmation("password")
    public void updateLogin(final User user, final String password, final String newLogin) {
        userRepository.updateLogin(user.getId(), user.getLogin(), password, newLogin);
    }

    @PrivateOnly
    @Confirmation("password")
    public void updateName(final User user, final String password, final String newName) {
        userRepository.updateName(user.getId(), user.getLogin(), password, newName);
    }

    @NoOuterAccess
    public void updatePasswordSha(final User user, final String password) {
        userRepository.updatePasswordSha(user.getId(), user.getLogin(), password);
    }

    public List<User> findAllByLogin(final String login) {
        return userRepository.findAllByLogin(login);
    }

    public long countAll() {
        return userRepository.count();
    }

    @SystemOnly
    @Confirmation("action")
    public void deleteById(final User user) {
        userRepository.deleteById(user.getId());
    }

    public boolean isLoginVacant(final String login) {
        return findAllByLogin(login).isEmpty();
    }
}
