package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
