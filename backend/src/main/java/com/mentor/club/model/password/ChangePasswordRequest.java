package com.mentor.club.model.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class ChangePasswordRequest {
    @Setter
    @Getter
    @JsonProperty("username")
    private String username;

    @Setter
    @Getter
    @JsonProperty("password")
    private String password;

    @Setter
    @Getter
    @JsonProperty("newPassword")
    private String newPassword;
}
