package com.mentor.club.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
public class RefreshToken extends ExpirableToken {
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
    private UUID userId;
}
