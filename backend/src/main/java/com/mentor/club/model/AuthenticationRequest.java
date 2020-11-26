package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class AuthenticationRequest {
    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "Username", required = true)
    String username;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "Password", required = true)
    String password;

}
