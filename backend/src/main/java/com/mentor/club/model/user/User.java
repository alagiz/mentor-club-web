package com.mentor.club.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    @Getter
    @Column(name = "id", columnDefinition = "uuid")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "username")
    @JsonProperty("username")
    private String username;

    @Column(name = "email")
    @JsonProperty("email")
    private String email;

    @Column(name = "hashedPassword")
    @JsonProperty("hashedPassword")
    private String hashedPassword;

    @Column(name = "thumbnailBase64")
    @JsonProperty("thumbnailBase64")
    private String thumbnailBase64;

    @Column(name = "status")
    @JsonProperty("status")
    private UserStatus userStatus = UserStatus.CREATED_UNCONFIRMED_EMAIL;
}
