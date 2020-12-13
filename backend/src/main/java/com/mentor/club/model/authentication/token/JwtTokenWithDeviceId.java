package com.mentor.club.model.authentication.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@Getter
@Setter
public abstract class JwtTokenWithDeviceId extends JwtToken {
    @Column(name = "deviceId")
    @JsonProperty("deviceId")
    private UUID deviceId;

    public JwtTokenWithDeviceId(JwtTokenType jwtTokenType) {
        super(jwtTokenType);
    }
}
