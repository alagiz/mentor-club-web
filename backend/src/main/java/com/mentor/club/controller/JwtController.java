package com.mentor.club.controller;

import com.mentor.club.service.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin()
@RestController
class JwtController {
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping(value = "/validate_jwt")
    @ApiOperation(value = "Validate JWT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid Token"),
            @ApiResponse(code = 401, message = "Invalid Token"),
    })
    public ResponseEntity validateJwt(@ApiParam(value = "'Bearer' Token for validation") @RequestHeader(name = "Authorization") String authorization) {
        return jwtService.validateJWT(authorization);
    }

    @GetMapping(value = "/validate_jwt/logout")
    @ApiOperation(value = "User logout - remove JWT from whitelist")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully de-whitelisted the token"),
            @ApiResponse(code = 400, message = "Invalid token"),
    })
    public ResponseEntity invalidateJwt(@ApiParam(value = "'Bearer' User token") @RequestHeader(name = "Authorization") String authorization) {
        return jwtService.invalidateJWT(authorization);
    }

    @GetMapping(value = "/user_id_from_jwt")
    @ApiOperation(value = "User id retrieval - get user id from jwt")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got user_id"),
            @ApiResponse(code = 400, message = "Invalid token"),
    })
    public ResponseEntity getUserIdFromJwt(@ApiParam(value = "'Bearer' User token") @RequestHeader(name = "Authorization") String authorization) {
        return jwtService.getUserIdFromJwt(authorization);
    }
}
