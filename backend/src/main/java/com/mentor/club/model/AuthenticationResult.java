package com.mentor.club.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

public class AuthenticationResult {
    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "The username the authentication was requested for", required = true)
    String id;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "The username the authentication was requested for", required = true)
    String username;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "A list of groups to which the user belongs to", required = true)
    List<String> group = new ArrayList<>();

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "The JWT generated", required = true)
    String token;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "The user display name", required = true)
    String displayName;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "The thumbnail photo link", required = true)
    String thumbnailPhoto;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "The resolution message (Ok/Invalid credentials)", required = true)
    String message;

    @Setter
    @Getter
    @JsonProperty
    @ApiModelProperty(notes = "Extra user information", required = true)
    ExtendableResult additionalInfo = new ExtendableResult();
}
