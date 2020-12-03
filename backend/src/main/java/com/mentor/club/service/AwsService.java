package com.mentor.club.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.mentor.club.model.user.User;
import com.mentor.club.model.aws.ILambdaRequest;
import com.mentor.club.model.aws.LambdaRequestSendEmailToConfirmEmail;
import com.mentor.club.model.aws.LambdaRequestSendEmailToResetPassword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AwsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsService.class);

    @Value("${aws.lambda.arn.confirm-email}")
    private String confirmEmailLambdaArn;

    @Value("${aws.lambda.access-key-id}")
    private String lambdaAccessKeyId;

    @Value("${aws.lambda.secret-access-key}")
    private String lambdaSecretAccessKey;

    public HttpStatus sendConfirmationEmail(String confirmationUrl, User user) {
        ILambdaRequest lambdaRequest = new LambdaRequestSendEmailToConfirmEmail(confirmationUrl, user.getEmail(), user.getUsername());

        return invokeSendConfirmationEmailLambda(lambdaRequest);
    }

    public HttpStatus sendPasswordResetEmail(String confirmationUrl, User user) {
        ILambdaRequest lambdaRequest = new LambdaRequestSendEmailToResetPassword(confirmationUrl, user.getEmail(), user.getUsername());

        return invokeSendConfirmationEmailLambda(lambdaRequest);
    }

    @LambdaFunction(functionName = "ses")
    private HttpStatus invokeSendConfirmationEmailLambda(ILambdaRequest lambdaRequest) {
        return invokeLambda(lambdaRequest, confirmEmailLambdaArn);
    }

    private HttpStatus invokeLambda(ILambdaRequest lambdaRequest, String lambdaArn) {
        try {
            String inputJSON = lambdaRequest.toJson(lambdaRequest);

            InvokeRequest lambdaRequestResult = new InvokeRequest()
                    .withFunctionName(lambdaArn)
                    .withPayload(inputJSON);

            lambdaRequestResult.setInvocationType(InvocationType.RequestResponse);

            // check how to pass region with env variable
            AWSCredentials credentials = new BasicAWSCredentials(lambdaAccessKeyId, lambdaSecretAccessKey);
            AWSLambda lambda = AWSLambdaClientBuilder.standard()
                    .withRegion(Regions.US_EAST_2)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();

            InvokeResult invokeResult = lambda.invoke(lambdaRequestResult);

            return HttpStatus.valueOf(invokeResult.getStatusCode());
        } catch (Exception exception) {
            LOGGER.error("Failed to execute lambda " + lambdaArn + ". Error: " + exception.getMessage());

            return HttpStatus.BAD_REQUEST;
        }
    }
}
