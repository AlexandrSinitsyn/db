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

    @JsonIgnore
    @OneToMany(mappedBy = "chat")
    @OrderBy("creationTime desc")
    private List<Message> messages;

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @OrderBy("creationTime desc")
    private List<User> users;

    @CreationTimestamp
    private Date creationTime;
}