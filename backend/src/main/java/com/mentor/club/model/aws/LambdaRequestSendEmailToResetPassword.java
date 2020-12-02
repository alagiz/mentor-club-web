package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendEmailToResetPassword extends LambdaRequestSendConfirmationEmail {
    @JsonProperty("messageBody")
    private String messageBody = "Click the button below to change your password. If you did not request password change at mentor-club, please ignore this email.\n" +
            "If you have any problem changing your password, feel free to email us at alagizov@gmail.com";

    @JsonProperty("buttonText")
    private String buttonText = "Change password";

    public LambdaRequestSendEmailToResetPassword(String confirmationUrl, String destinationEmail, String username) {
        super(confirmationUrl, destinationEmail, username);
    }
}
