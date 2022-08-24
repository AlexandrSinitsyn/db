package com.server.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "server_user",
        uniqueConstraints = @UniqueConstraint(columnNames = "login"),
        indexes = @Index(columnList = "creationTime"))
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "User entity")
public class User implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]{2,30}")
    private String name;

    @NotNull
    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_-]{2,30}")
    private String login;

    private boolean admin;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    @OrderBy("creationTime desc")
    private List<Message> messages;

    @JsonIgnore
    @ManyToMany(mappedBy = "users")
    @OrderBy("creationTime desc")
    private List<Chat> chats;

    @CreationTimestamp
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date creationTime;

    @Override
    public boolean checkPrivacy(final User user) {
        return user != null && user.id == this.id;
    }
}