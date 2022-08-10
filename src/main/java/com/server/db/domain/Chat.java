package com.server.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "server_chat",
        indexes = @Index(columnList = "creationTime"))
@Getter
@Setter
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Date creationTime;

    public void setUsers(final List<User> users) {
        this.users = users;
        this.logins = users.stream().map(User::getLogin).toList();
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
}