package com.mentor.club.service;

import com.mentor.club.exception.InternalException;
import com.mentor.club.model.authentication.token.concretes.PasswordResetToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import com.mentor.club.model.error.HttpCallError;
import com.mentor.club.model.user.User;
import com.mentor.club.repository.IPasswordResetTokenRepository;
import com.mentor.club.repository.IUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"backend.deployment.url=test-url"})
@RunWith(MockitoJUnitRunner.class)
public class PasswordServiceTest {
    @InjectMocks
    private PasswordService passwordService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private AwsService awsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private IPasswordResetTokenRepository passwordResetTokenRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_isProvidedPasswordCorrect_checksPasswordCorrectly() {
        String password = "test-password";
        String wrongPassword = "wrong-password";
        String hashedPassword = "$2a$04$/UljyCSLBRCd0cb9H0mCquoR6ZsR68FJe1sXXXzIIwlCAymMyfNZ.";

        assertTrue(passwordService.isProvidedPasswordCorrect(password, hashedPassword));
        assertFalse(passwordService.isProvidedPasswordCorrect(wrongPassword, hashedPassword));
    }

    @Test
    public void test_hashPassword_hashesPassword() {
        String password = "test-password";
        String hashedPassword = passwordService.hashPassword(password);

        assertTrue(passwordService.isProvidedPasswordCorrect(password, hashedPassword));
    }

    @Test
    public void test_generateResetForgottenPasswordEmail_ifNoUserIsFound_returnsResponseWithStatusNotFound() {
        String userEmail = "test-email";
        User user = new User();

        user.setEmail(userEmail);

        Optional<User> userWithGivenEmail = Optional.empty();

        when(userRepository.findUserByEmail(userEmail)).thenReturn(userWithGivenEmail);

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void test_generateResetForgottenPasswordEmail_ifExceptionInThrownInUserRepository_returnsResponseWithStatusInternalServerError() {
        String userEmail = "test-email";

        when(userRepository.findUserByEmail(userEmail)).thenThrow(new InternalException(HttpStatus.BAD_REQUEST, HttpCallError.FAILED_TO_FIND_USER));

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void test_generateResetForgottenPasswordEmail_ifExceptionInThrownInPasswordResetTokenRepository_returnsResponseWithStatusInternalServerError() {
        String userEmail = "test-email";
        User user = new User();

        user.setEmail(userEmail);

        Optional<User> userWithGivenEmail = Optional.of(user);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(userWithGivenEmail);
        when(passwordResetTokenRepository.save(any())).thenThrow(new InternalException(HttpStatus.BAD_REQUEST, HttpCallError.FAILED_TO_SAVE_TO_DB));

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void test_generateResetForgottenPasswordEmail_ifExceptionInThrownInAwsService_returnsResponseWithStatusInternalServerError() {
        String userEmail = "test-email";
        User user = new User();

        user.setEmail(userEmail);

        Optional<User> userWithGivenEmail = Optional.of(user);
        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);

        when(userRepository.findUserByEmail(userEmail)).thenReturn(userWithGivenEmail);
        when(passwordResetTokenRepository.save(any())).thenReturn(passwordResetToken);
        when(awsService.sendPasswordResetEmail(any(), any())).thenThrow(new InternalException(HttpStatus.BAD_REQUEST, HttpCallError.SERVICE_UNAVAILABLE));

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void test_generateResetForgottenPasswordEmail_ifUserIsFound_returnsResponseWithStatusOK() {
        String userEmail = "test-email";
        User user = new User();

        user.setEmail(userEmail);

        Optional<User> userWithGivenEmail = Optional.of(user);
        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);
        HttpStatus testHttpStatus = HttpStatus.ACCEPTED;

        passwordResetToken.setToken("testToken");

        when(userRepository.findUserByEmail(userEmail)).thenReturn(userWithGivenEmail);
        when(passwordResetTokenRepository.save(any())).thenReturn(passwordResetToken);
        when(awsService.sendPasswordResetEmail(any(), any())).thenReturn(testHttpStatus);

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
