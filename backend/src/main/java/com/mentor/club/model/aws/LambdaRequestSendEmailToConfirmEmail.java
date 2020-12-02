package com.mentor.club.model.aws;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LambdaRequestSendEmailToConfirmEmail extends LambdaRequestSendConfirmationEmail {
    @JsonProperty("messageBody")
    private String messageBody = "Click the button below to confirm your email address. If you did not register at mentor-club with this email address, please ignore this email.\n" +
            "If you have any problem verifying your account, feel free to email us at alagizov@gmail.com";

    @JsonProperty("buttonText")
    private String buttonText = "Verify email";

    public LambdaRequestSendEmailToConfirmEmail(String confirmationUrl, String destinationEmail, String username) {
        super(confirmationUrl, destinationEmail, username);
    }
}
