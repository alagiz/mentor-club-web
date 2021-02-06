package com.mentor.club.model.authentication;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.mentor.club.model.ExtendableResult;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthenticationResult {
    @JsonProperty
    @ApiModelProperty(notes = "The username the authentication was requested for", required = true)
    String id;

    @JsonProperty
    @ApiModelProperty(notes = "The username the authentication was requested for", required = true)
    String username;

    @JsonProperty
    @ApiModelProperty(notes = "The email of the user")
    String email;

    @JsonProperty
    @ApiModelProperty(notes = "A list of groups to which the user belongs to", required = true)
    List<String> group = new ArrayList<>();

    @JsonProperty
    @ApiModelProperty(notes = "The JWT generated", required = true)
    String token;

    @JsonProperty
    @ApiModelProperty(notes = "The user display name", required = true)
    String displayName;

    @JsonProperty
    @ApiModelProperty(notes = "The thumbnail photo link", required = true)
    String thumbnailPhoto;

    @JsonProperty
    @ApiModelProperty(notes = "The resolution message (Ok/Invalid credentials)", required = true)
    String message;

    @JsonProperty
    @ApiModelProperty(notes = "Extra user information", required = true)
    ExtendableResult additionalInfo = new ExtendableResult();
}
