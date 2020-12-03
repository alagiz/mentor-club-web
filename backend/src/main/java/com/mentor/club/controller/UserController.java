package com.mentor.club.controller;

import com.mentor.club.model.authentication.AuthenticationRequest;
import com.mentor.club.model.password.ChangeForgottenPasswordRequest;
import com.mentor.club.model.password.ChangePasswordRequest;
import com.mentor.club.model.user.NewUser;
import com.mentor.club.service.UserService;
import io.swagger.annotations.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin()
@RestController
@RequestMapping("/user")
@Api(value = "User authentication")
public class UserController {
    @Getter
    @Setter
    protected UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @RequestMapping("/authenticate")
    @ApiOperation(value = "Credentials-based authentication request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 404, message = "User not found"),
    })
    public ResponseEntity authenticate(@ApiParam(value = "Credentials in JSON format 'username/password'")
                                       @RequestBody AuthenticationRequest authentication) {
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
    @RequestMapping("/confirm-email/{userId}")
    @ApiOperation(value = "Confirm email address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 404, message = "User id not found"),
    })
    public ResponseEntity confirmEmail(@ApiParam(value = "Confirm email") @PathVariable UUID userId) {
        return userService.confirmEmail(userId);
    }

    @GetMapping
    @RequestMapping("/reset-password/{userId}")
    @ApiOperation(value = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 404, message = "User id not found"),
    })
    public ResponseEntity resetPassword(@ApiParam(value = "Confirm email") @PathVariable UUID userId) {
        return userService.confirmEmail(userId);
    }

    @PostMapping
    @RequestMapping("/reset-password")
    @ApiOperation(value = "Reset password request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 404, message = "User not found"),
    })
    public ResponseEntity resetPassword(@ApiParam(value = "Reset password") @RequestParam String email) {
        return userService.resetPassword(email);
    }

    @PostMapping
    @RequestMapping("/change-forgotten-password")
    @ApiOperation(value = "Change forgotten password request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Invalid password"),
    })
    public ResponseEntity changeForgottenPassword(@ApiParam(value = "Reset password")
                                                  @RequestBody ChangeForgottenPasswordRequest changeForgottenPasswordRequest) {
        return userService.changeForgottenPassword(changeForgottenPasswordRequest);
    }

    @PostMapping
    @RequestMapping("/change-password")
    @ApiOperation(value = "Change password request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Invalid password"),
    })
    public ResponseEntity changePassword(@ApiParam(value = "Reset password")
                                         @RequestBody ChangePasswordRequest changePasswordRequest,
                                         @RequestHeader(name = "Authorization") String authorization) {
        return userService.changePassword(changePasswordRequest, authorization);
    }

    @PostMapping
    @RequestMapping("/logout")
    @ApiOperation(value = "Logout user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Unauthorized"),
    })
    public ResponseEntity logout(@ApiParam(value = "Logout user")
                                 @RequestHeader(name = "Authorization") String authorization,
                                 @RequestParam String username) {
        return userService.logout(authorization, username);
    }

    @GetMapping
    @RequestMapping("/public-key")
    @ApiOperation(value = "Request the public key")
    public ResponseEntity getPublicKey() {
        return userService.getPublicKey();
    }
}
