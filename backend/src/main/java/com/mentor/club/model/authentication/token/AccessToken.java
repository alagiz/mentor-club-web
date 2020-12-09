package com.mentor.club.model.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@NoArgsConstructor
public class AccessToken extends JwtToken {
    @Column(name = "deviceId")
    @JsonProperty("deviceId")
    private UUID deviceId;

    public AccessToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
