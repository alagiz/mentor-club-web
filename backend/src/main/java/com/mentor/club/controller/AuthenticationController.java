package com.mentor.club.controller;

import com.mentor.club.service.AbstractAuthenticationService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mentor.club.model.AuthenticationRequest;
import com.mentor.club.model.InternalResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin()
@RestController
@RequestMapping("/authenticate")
@Api(value = "User authentication")
public class AuthenticationController {
    @Getter
    @Setter
    protected AbstractAuthenticationService authenticationService;

    @PostMapping()
    @ApiOperation(value = "Credentials-based authentication request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity authenticate(@ApiParam(value = "Credentials in JSON format 'username/password'") @RequestBody AuthenticationRequest authentication) {
        final InternalResponse authResponse = authenticationService.authenticate(authentication.getUsername(), authentication.getPassword());
        if (authResponse.getStatus() == HttpStatus.OK.value()) {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(authResponse.getJson(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping()
    @ApiOperation(value = "Request the public key")
    public ResponseEntity getPublicKey() {
        return new ResponseEntity<>(authenticationService.getPublicKey(), HttpStatus.OK);
    }
}
