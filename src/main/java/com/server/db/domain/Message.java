package com.server.db.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "server_message",
        indexes = @Index(columnList = "creationTime"))
@Getter
@Setter
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotBlank
    @Size(min = 1, max = 65000)
    @Lob
    private String text;

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
    private Date creationTime;
}