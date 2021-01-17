package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

class LambdaRequestSendInformationalEmail implements ILambdaRequest {
    @Getter
    @Setter
    @JsonProperty("destinationEmail")
    private String destinationEmail;

    @Getter
    @Setter
    @JsonProperty("username")
    private String username;

    LambdaRequestSendInformationalEmail(String destinationEmail, String username) {
        this.destinationEmail = destinationEmail;
        this.username = username;
    }
}
