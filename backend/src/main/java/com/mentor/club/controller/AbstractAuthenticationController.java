package com.mentor.club.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mentor.club.service.AbstractAuthenticationService;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractAuthenticationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAuthenticationController.class);

    @Getter
    @Setter
    protected AbstractAuthenticationService authenticationService;

    @Autowired
    public AbstractAuthenticationController() {
        final String authenticationMode = PropertiesManager.getProperty("authentication_mode");
        boolean initializationFailed = false;
        switch (authenticationMode) {
            case "InternalAuthentication":
                this.authenticationService = new InternalAuthenticationService();
                break;
            default:
                initializationFailed = true;
                LOGGER.warn("Initialization of authentication service failed - authentication_mode property not properly set. Reverting to default (LDAP).");
                this.authenticationService = new LdapAuthenticationService();
                break;
        }

        if (!initializationFailed) {
            LOGGER.info("Initialization of {} successful : authentication_mode set to {}.", qualifiedControllerName(), authenticationMode);
        }
    }

    abstract String qualifiedControllerName();
}
