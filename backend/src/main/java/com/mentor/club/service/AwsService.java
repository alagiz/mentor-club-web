package com.mentor.club.service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.invoke.LambdaFunction;
import com.amazonaws.services.lambda.model.InvocationType;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.google.gson.Gson;
import com.mentor.club.model.aws.LambdaRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AwsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AwsService.class);

    @Value("${aws.lambda.confirm-email.arn}")
    private String confirmEmailLambdaArn;

    @LambdaFunction(functionName = "ses")
    public HttpStatus sendConfirmationEmail(String confirmationUrl, String email) {
        try {
            LambdaRequest lambdaRequest = new LambdaRequest();

            lambdaRequest.setEmail(email);
            lambdaRequest.setConfirmationUrl(confirmationUrl);

            String inputJSON = new Gson().toJson(lambdaRequest);

            InvokeRequest lambdaRequestResult = new InvokeRequest()
                    .withFunctionName(confirmEmailLambdaArn)
                    .withPayload(inputJSON);

            lambdaRequestResult.setInvocationType(InvocationType.RequestResponse);

            // check how to pass region with env variable
            // check if credentials need to be set when ec2 already has permissions, code for that:
            // =====================================================================================
            //    final String AWS_ACCESS_KEY_ID = "xx";
            //    final String AWS_SECRET_ACCESS_KEY = "xx";
            //
            //    AWSCredentials credentials = new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY);
            //    AWSLambda lambda = AWSLambdaClientBuilder.standard()
            //        .withRegion(Regions.US_EAST_1)
            //        .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
            // =====================================================================================
            AWSLambda lambda = AWSLambdaClientBuilder
                    .standard()
                    .withRegion(Regions.US_EAST_2)
                    .build();

            InvokeResult invokeResult = lambda.invoke(lambdaRequestResult);

            return HttpStatus.valueOf(invokeResult.getStatusCode());
        } catch (Exception exception) {
            LOGGER.error("Failed to send confirmation email to " + email + ". Error: " + exception.getMessage());

            return HttpStatus.BAD_REQUEST;
        }
    }
}
