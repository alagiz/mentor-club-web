package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendEmailToResetPassword extends LambdaRequestSendConfirmationEmail {
    @JsonProperty("textMessageBody")
    private String textMessageBody = "Hi ${event.username}!\n\nClick the following url to change your password at mentor-club: ${event.confirmationUrl}\n\n\nIf you have any problems changing password, feel free to email us at alagizov@gmail.com";

    @JsonProperty("messageBody")
    private String messageBody = "Click the button below to <span class=\"il\">change</span> your password. If you did not request password change at mentor-club, please ignore this email.";

    @JsonProperty("emailTopic")
    private String emailTopic = "Change password request at Mentor-club";

    @JsonProperty("buttonText")
    private String buttonText = "Change password";

    public LambdaRequestSendEmailToResetPassword(String confirmationUrl, String destinationEmail, String username) {
        super(confirmationUrl, destinationEmail, username);
    }
}
