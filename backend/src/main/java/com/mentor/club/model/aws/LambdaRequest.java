package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class LambdaRequest {
    @Getter
    @Setter
    @JsonProperty("confirmationUrl")
    private String confirmationUrl;

    @Getter
    @Setter
    @JsonProperty("email")
    private String email;
}
