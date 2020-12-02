package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class LambdaRequestSendConfirmationEmail implements ILambdaRequest {
    @Getter
    @Setter
    @JsonProperty("confirmationUrl")
    private String confirmationUrl;

    @Getter
    @Setter
    @JsonProperty("destinationEmail")
    private String destinationEmail;

    @Getter
    @Setter
    @JsonProperty("username")
    private String username;

    public LambdaRequestSendConfirmationEmail(String confirmationUrl, String destinationEmail, String username) {
        this.confirmationUrl = confirmationUrl;
        this.destinationEmail = destinationEmail;
        this.username = username;
    }
}
