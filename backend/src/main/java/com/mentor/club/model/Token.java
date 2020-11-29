package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Token {
    @Id
    @Column(name = "id")
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "token")
    @JsonProperty("token")
    private String token;

    @Column(name = "userId")
    @JsonProperty("userId")
    private String userId;

    @Column(name = "expiresAt")
    @JsonProperty("expiresAt")
    private String expiresAt;
}
