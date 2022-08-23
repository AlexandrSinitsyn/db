package com.server.db.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "server_user_chats",
            joinColumns = @JoinColumn(name = "chat_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @OrderBy("creationTime desc")
    private List<User> users;

    @JsonIgnore
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    @OrderBy("creationTime desc")
    private List<Message> messages;

    @CreationTimestamp
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date creationTime;

    public void addUser(final User user) {
        users.add(user);
    }

    public void removeUser(final User user) {
        users.remove(user);
    }

    public void addMessage(final Message message) {
        messages.add(message);
    }

    @Override
    public boolean checkPrivacy(final User user) {
        return user != null && admin != null && admin.equals(user.getLogin());
    }
}