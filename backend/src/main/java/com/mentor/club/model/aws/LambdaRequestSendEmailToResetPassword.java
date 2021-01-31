package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendEmailToResetPassword extends LambdaRequestSendConfirmationEmail {
    @JsonProperty("textMessageBody")
    private String textMessageBody = "Hi ${event.username}!\n\nHere is your code: ${event.securityCode}\n\n\nIf you have any problems changing password, feel free to email us at alagizov@gmail.com";

    @JsonProperty("messageBody")
    private String messageBody = "It seems to me that you requested password reset... Use the code below to verify this action.";

    @JsonProperty("messageAfterBody")
    private String messageAfterBody = "If you did not request password change at Run-pact, please ignore this email.";

    @JsonProperty("emailTopic")
    private String emailTopic = "Change password request at Run-pact";

    @JsonProperty("buttonText")
    private String buttonText = "Change password";

    public LambdaRequestSendEmailToResetPassword(String confirmationUrl, String destinationEmail, String username, String securityCode) {
        super(confirmationUrl, destinationEmail, username, securityCode);
    }
}
