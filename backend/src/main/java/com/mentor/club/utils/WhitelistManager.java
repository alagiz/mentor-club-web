package com.mentor.club.utils;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.interfaces.DecodedJWT;

public class WhitelistManager {

    private WhitelistManager() {
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(WhitelistManager.class);

    // <username, token>
    private static final Map<String, String> whiteList = new HashMap<>();

    public static boolean isTokenWhitelisted(DecodedJWT decodedJWT) {
        final String username = decodedJWT.getClaim("username").asString();

        if (whitelistContainsToken(username, decodedJWT.getToken())
                && decodedJWT.getExpiresAt().after(Date.from(Instant.now()))) {
            return true;
        }
        removeToken(username);
        return false;
    }

    /**
     * Check that the token for this user is already in the whitelist
     *
     * @param username the user
     * @param token    the user associated token
     * @return true if the entry is in the WL with the associated token, otherwise delete the entry and return false
     */
    private static boolean whitelistContainsToken(String username, String token) {
        final String whitelistToken = whiteList.get(username);
        if (whitelistToken != null) {
            if (whitelistToken.equals(token)) {
                return true;
            } else {
                LOGGER.warn("Invalid JWT received for validation, but a different JWT is whitelisted. ({}).", token);
            }
        }
        return false;
    }

    /**
     * Add the user token in the whitelist, replace any existing entry
     *
     * @param user  the user
     * @param token the user associated token
     */
    public static void addToken(String user, String token) {
        synchronized (WhitelistManager.class) {
            whiteList.put(user, token);
        }
    }

    public static boolean logout(DecodedJWT decodedJWT) {
        return removeToken(decodedJWT.getClaim("username").asString());
    }

    /**
     * Remove one of the whitelist entries
     *
     * @param user the associated user token to be removed
     * @return true if the token has been found and successfully removed
     */
    private static boolean removeToken(String user) {
        synchronized (WhitelistManager.class) {
            return whiteList.remove(user) != null;
        }
    }
}
