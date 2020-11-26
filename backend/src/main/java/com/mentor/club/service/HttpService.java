package com.mentor.club.service;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mentor.club.exception.ExternalHttpCallException;
import com.mentor.club.exception.InternalException;
import com.mentor.club.model.error.HttpCallError;

/**
 * HttpService.
 */
@Service
public class HttpService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpService.class);

    InputStream executeRequest(HttpUriRequest request, HttpCallError possibleError, String serviceUnavailableErrorDescription) {
        final HttpClient httpClient = HttpClientBuilder.create().build();

        try {
            final HttpResponse response = httpClient.execute(request);

            validateStatusCode(response);

            return response.getEntity().getContent();
        } catch (ExternalHttpCallException e) {
            throw e;
        } catch (IOException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new InternalException(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, HttpCallError.SERVICE_UNAVAILABLE, serviceUnavailableErrorDescription);
        } catch (RuntimeException e) {
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new InternalException(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, possibleError);
        }
    }

    /**
     * Validate the status code of the given http-response.
     * Throw a ResponseStatusException if the status-code is not 200, with the given error message.
     *
     * @param response the response that should be validated.
     */
    static void validateStatusCode(HttpResponse response) {
        final int statusCode = response.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK) {
            throw new ExternalHttpCallException(response);
        }
    }
}