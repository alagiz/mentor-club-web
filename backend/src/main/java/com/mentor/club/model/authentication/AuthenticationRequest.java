package com.mentor.club.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class AuthenticationRequest {
    @JsonProperty
    @ApiModelProperty(notes = "Username", required = true)
    String username;

    @JsonProperty
    @ApiModelProperty(notes = "Password", required = true)
    String password;

    @JsonProperty("deviceId")
    @ApiModelProperty(notes = "Device id", required = true)
    private UUID deviceId;
}
