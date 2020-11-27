package com.mentor.club.controller;

import com.mentor.club.model.AuthenticationRequest;
import com.mentor.club.service.AuthenticationService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
@RequestMapping("/authenticate")
@Api(value = "User authentication")
public class AuthenticationController {
    @Getter
    @Setter
    protected AuthenticationService authenticationService;

    @PostMapping()
    @ApiOperation(value = "Credentials-based authentication request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity authenticate(@ApiParam(value = "Credentials in JSON format 'username/password'") @RequestBody AuthenticationRequest authentication) {
        return authenticationService.authenticate(authentication);
    }

    @GetMapping()
    @ApiOperation(value = "Request the public key")
    public ResponseEntity getPublicKey() {
        return authenticationService.getPublicKey();
    }
}
