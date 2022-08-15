package com.server.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "server_chat",
        indexes = @Index(columnList = "creationTime"))
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Chat entity")
public class Chat implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    private String admin;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy("creationTime desc")
    private List<User> users;

    @Transient
    private List<String> logins;

    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    @OrderBy("creationTime desc")
    private List<Message> messages;

    @CreationTimestamp
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date creationTime;

    public void setUsers(final List<User> users) {
        this.users = users;
        this.logins = users.stream().map(User::getLogin).toList();
    }

    public List<String> getLogins() {
        if (logins.size() != users.size()) {
            setUsers(users);
        }

        return logins;
    }

    public void addUser(final User user) {
        users.add(user);
        logins.add(user.getLogin());
    }

    public void removeUser(final User user) {
        users.remove(user);
        logins.remove(user.getLogin());
    }

    public void addMessage(final Message message) {
        messages.add(message);
    }

    @Override
    public boolean checkPrivacy(final User user) {
        return user != null && Objects.equals(user.getLogin(), admin);
    }
}