package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendEmailToConfirmEmail extends LambdaRequestSendConfirmationEmail {
    @JsonProperty("textMessageBody")
    private String textMessageBody = "Hi ${event.username}!\n\nHere is your code: ${event.securityCode}.\n\n\nIf you have any problems verifying your account, feel free to email us at alagizov@gmail.com";

    @JsonProperty("messageBody")
    private String messageBody = "You registered at Run-pact, it would appear! Use the code below to verify your email.";

    @JsonProperty("messageAfterBody")
    private String messageAfterBody = "If you did not register at Run-pact with this email address, please ignore this motherfucking email.";

    @JsonProperty("emailTopic")
    private String emailTopic = "Verify your email for Run-pact";

    @JsonProperty("buttonText")
    private String buttonText = "Verify email";

    public LambdaRequestSendEmailToConfirmEmail(String confirmationUrl, String destinationEmail, String username, String securityCode) {
        super(confirmationUrl, destinationEmail, username, securityCode);
    }
}
