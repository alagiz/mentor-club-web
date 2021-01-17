package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendPasswordChangedSuccessfullyEmail extends LambdaRequestSendInformationalEmail {
    @JsonProperty("textMessageBody")
    private String textMessageBody = "Hi ${event.username}!\n\nYour password has been changed successfully!";

    @JsonProperty("messageBody")
    private String messageBody = "Your password has been changed successfully! If you didn't request this, please contact us immediately.";

    @JsonProperty("emailTopic")
    private String emailTopic = "Password changed";

    public LambdaRequestSendPasswordChangedSuccessfullyEmail(String destinationEmail, String username) {
        super(destinationEmail, username);
    }
}
