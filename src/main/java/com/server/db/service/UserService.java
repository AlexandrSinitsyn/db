package com.server.db.service;

import com.server.db.Tools;
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

    public User findByLoginAndPassword(final String login, final String password) {
        return login == null || password == null ? null
                : userRepository.findByLoginAndPassword(login, password);
    }

    public User findById(final long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void makeAdmin(final User user) {
        user.setAdmin(true);
        save(user);
    }

    public void downgrade(final User user) {
        user.setAdmin(false);
        save(user);
    }

    public User save(final User user) {
        return userRepository.save(user);
    }

    @Confirmation("password")
    public String updateLogin(final User user, final String password, final String newLogin) {
        userRepository.updateLogin(user.getId(), user.getLogin(), password, newLogin);

        return Tools.SUCCESS_RESPONSE;
    }

    @Confirmation("password")
    public String updateName(final User user, final String password, final String newName) {
        userRepository.updateName(user.getId(), user.getLogin(), password, newName);

        return Tools.SUCCESS_RESPONSE;
    }

    public void updatePasswordSha(final User user, final String password) {
        userRepository.updatePasswordSha(user.getId(), user.getLogin(), password);
    }

    public List<User> findAllByLogin(final String login) {
        return userRepository.findAllByLogin(login);
    }

    public long countAll() {
        return userRepository.count();
    }

    @Confirmation("action")
    public String deleteById(final User user) {
        userRepository.deleteById(user.getId());

        return Tools.SUCCESS_RESPONSE;
    }

    public boolean isLoginVacant(final String login) {
        return findAllByLogin(login).isEmpty();
    }
}
