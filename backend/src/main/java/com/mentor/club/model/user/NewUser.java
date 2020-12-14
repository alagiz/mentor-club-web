package com.mentor.club.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class NewUser {
    @Getter
    @Setter
    @JsonProperty("name")
    private String name;

    @Getter
    @Setter
    @JsonProperty("username")
    private String username;

    @Getter
    @Setter
    @JsonProperty("email")
    private String email;

    @Getter
    @Setter
    @JsonProperty("password")
    private String password;

    @Getter
    @Setter
    @JsonProperty("thumbnailBase64")
    private String thumbnailBase64;
}
