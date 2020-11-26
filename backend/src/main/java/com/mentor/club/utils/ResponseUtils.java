package com.mentor.club.utils;

import java.util.List;

import com.mentor.club.model.AuthenticationResult;
import com.mentor.club.model.ExtendableResult;

public class ResponseUtils {

    private ResponseUtils() {
    }

    public static AuthenticationResult unauthorized(String username) {
        final AuthenticationResult response = new AuthenticationResult();
        response.setUsername(username);
        response.setMessage("Invalid Credentials");
        return response;
    }

    public static AuthenticationResult authOk(String username, String displayName, String thumbnailPhoto, List<String> group, ExtendableResult extendableResult) {
        final AuthenticationResult authenticationResult = authOk(username, displayName, thumbnailPhoto, group);
        authenticationResult.setAdditionalInfo(extendableResult);
        return authenticationResult;
    }

    public static AuthenticationResult authOk(String username, String displayName, String thumbnailPhoto, List<String> group) {
        final String token = RsaUtils.generateToken(username, group);
        WhitelistManager.addToken(username, token);

        final AuthenticationResult result = new AuthenticationResult();
        result.setId(username);
        result.setUsername(username);
        result.setToken(token);
        result.setDisplayName(displayName);
        result.setThumbnailPhoto(thumbnailPhoto);
        result.setGroup(group);
        result.setMessage("OK");
        return result;
    }
}
