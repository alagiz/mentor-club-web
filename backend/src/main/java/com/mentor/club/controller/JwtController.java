package com.mentor.club.controller;

import com.mentor.club.service.JwtService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/token")
class JwtController {
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping(value = "/validate-jwt")
    @ApiOperation(value = "Validate JWT")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Valid Token"),
            @ApiResponse(code = 401, message = "Invalid Token"),
    })
    public ResponseEntity validateJwt(@ApiParam(value = "'Bearer' Token for validation") @RequestHeader(name = "Authorization") String authorization) {
        return jwtService.validateAccessToken(authorization);
    }

    @GetMapping
    @RequestMapping("/new-access-token")
    @ApiOperation(value = "Get new access and refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized"),
            @ApiResponse(code = 401, message = "Invalid password"),
    })
    public ResponseEntity getRefreshAndAccessToken(@CookieValue("refreshToken") String refreshTokenCookie,
                                                   @RequestHeader(name = "Authorization") Optional<String> authorization,
                                                   @RequestParam UUID deviceId,
                                                   HttpServletResponse response) {
        return jwtService.getRefreshAndAccessToken(refreshTokenCookie, authorization, deviceId, response);
    }
}
