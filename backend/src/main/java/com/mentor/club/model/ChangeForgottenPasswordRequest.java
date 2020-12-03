package com.mentor.club.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class ChangeForgottenPasswordRequest {
    @Setter
    @Getter
    @JsonProperty("newPassword")
    private String newPassword;

    @Setter
    @Getter
    @JsonProperty("passwordResetToken")
    private String passwordResetToken;
}
