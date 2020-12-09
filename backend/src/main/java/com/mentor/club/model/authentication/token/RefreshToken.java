package com.mentor.club.model.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends JwtToken {
    @Column(name = "deviceId")
    @JsonProperty("deviceId")
    private UUID deviceId;

    public RefreshToken(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
