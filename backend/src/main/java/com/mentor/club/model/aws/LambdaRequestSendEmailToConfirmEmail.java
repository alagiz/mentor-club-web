package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendEmailToConfirmEmail extends LambdaRequestSendConfirmationEmail {
    @JsonProperty("textMessageBody")
    private String textMessageBody = "Hi ${event.username}!\n\nClick the following url to verify your registration at mentor-club: ${event.confirmationUrl}\n\n\nIf you have any problems verifying your account, feel free to email us at alagizov@gmail.com";

    @JsonProperty("messageBody")
    private String messageBody = "Click the button below to <span class=\"il\">confirm</span> your email address. If you did not register at mentor-club with this email address, please ignore this email.";

    @JsonProperty("emailTopic")
    private String emailTopic = "Verify your email for Mentor-club";

    @JsonProperty("buttonText")
    private String buttonText = "Verify email";

    public LambdaRequestSendEmailToConfirmEmail(String confirmationUrl, String destinationEmail, String username) {
        super(confirmationUrl, destinationEmail, username);
    }
}
