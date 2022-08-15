package com.server.db.domain;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Table(name = "server_message",
        indexes = @Index(columnList = "creationTime"))
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Message entity")
public class Message implements DbEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 65000)
    @Lob
    private String text;

    private boolean unread;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "message_id")
    private List<Message> links;

    @CreationTimestamp
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Date creationTime;

    @Override
    public boolean checkPrivacy(final User user) {
        return user != null && user.getId() == this.author.getId();
    }
}