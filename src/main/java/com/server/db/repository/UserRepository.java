package com.server.db.repository;

import com.server.db.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM server_user WHERE login=?1 AND passwordSHA=SHA1(CONCAT('1be3db47a7684152', ?1, ?2))", nativeQuery = true)
    User findByLoginAndPassword(String login, String password);

    @Transactional
    @Modifying
    @Query(value = "UPDATE server_user SET login=?4 WHERE id=?1 AND login=?2 AND passwordSHA=SHA1(CONCAT('1be3db47a7684152', ?2, ?3))", nativeQuery = true)
    void updateLogin(long id, String login, String password, String newLogin);

    @Transactional
    @Modifying
    @Query(value = "UPDATE server_user SET name=?4 WHERE id=?1 AND login=?2 AND passwordSHA=SHA1(CONCAT('1be3db47a7684152', ?2, ?3))", nativeQuery = true)
    void updateName(long id, String login, String password, String newName);

    @Transactional
    @Modifying
    @Query(value = "UPDATE server_user SET passwordSha=SHA1(CONCAT('1be3db47a7684152', ?2, ?3)) WHERE id=?1", nativeQuery = true)
    void updatePasswordSha(long id, String login, String password);

    List<User> findAllByLogin(String login);

    List<User> findAllByOrderByCreationTimeDesc();
}