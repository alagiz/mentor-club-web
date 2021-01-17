package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendConfirmationSuccessfulEmail extends LambdaRequestSendInformationalEmail {
    @JsonProperty("textMessageBody")
    private String textMessageBody = "Hi ${event.username}!\n\nEmail confirmed successfully!\n\n\nWelcome to the app!";

    @JsonProperty("messageBody")
    private String messageBody = "Email confirmed successfully! Welcome to the app!";

    @JsonProperty("emailTopic")
    private String emailTopic = "Welcome to the app!";

    public LambdaRequestSendConfirmationSuccessfulEmail(String destinationEmail, String username) {
        super(destinationEmail, username);
    }
}
