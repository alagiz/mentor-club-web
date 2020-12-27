package com.mentor.club.controller;

import com.mentor.club.service.JwtService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JwtControllerTest {
    @InjectMocks
    private JwtController jwtController;

    @Mock
    private JwtService jwtService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_validateAccessToken_callsValidateAccessTokenOfJwtService() {
        String authorization = "test-authorization";

        jwtController.validateAccessToken(authorization);

        verify(jwtService, times(1)).validateAccessToken(authorization);
    }

    @Test
    public void test_getNewAccessAndRefreshToken_callsGetNewAccessAndRefreshTokenOfJwtService() {
        Optional<String> authorization = Optional.of("test-authorization");
        String refreshTokenCookie = "test-refresh-token-cookie";
        UUID deviceId = UUID.randomUUID();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        jwtController.getNewAccessAndRefreshToken(refreshTokenCookie, authorization, deviceId, httpServletResponse);

        verify(jwtService, times(1)).getNewAccessAndRefreshToken(refreshTokenCookie, authorization, deviceId, httpServletResponse);
    }

    @Test
    public void test_getPublicKey_callsGetPublicKeyOfJwtService() {
        jwtController.getPublicKey();

        verify(jwtService, times(1)).getPublicKey();
    }
}
