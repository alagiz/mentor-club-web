package com.mentor.club.controller;

import com.mentor.club.model.AuthenticationRequest;
import com.mentor.club.model.NewUser;
import com.mentor.club.service.UserService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin()
@RestController
@RequestMapping("/user")
@Api(value = "User authentication")
public class UserController {
    @Getter
    @Setter
    protected UserService userService;

    @PostMapping
    @RequestMapping("/authenticate")
    @ApiOperation(value = "Credentials-based authentication request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "User not found"),
    })
    public ResponseEntity authenticate(@ApiParam(value = "Credentials in JSON format 'username/password'") @RequestBody AuthenticationRequest authentication) {
        return userService.authenticate(authentication);
    }

    @PostMapping
    @ApiOperation(value = "New user request")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity createNewUser(@ApiParam(value = "New user request") @RequestBody NewUser newUser) {
        return userService.createNewUser(newUser);
    }

    @GetMapping
    @RequestMapping("/confirm-email")
    @ApiOperation(value = "Confirm email address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity confirmEmail(@ApiParam(value = "Confirm email") @PathVariable String userId) {
        return userService.confirmEmail(userId);
    }

    @GetMapping
    @RequestMapping("/public-key")
    @ApiOperation(value = "Request the public key")
    public ResponseEntity getPublicKey() {
        return userService.getPublicKey();
    }
}
