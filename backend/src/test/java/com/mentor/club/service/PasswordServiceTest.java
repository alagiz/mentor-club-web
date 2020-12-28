package com.mentor.club.service;

import com.mentor.club.exception.InternalException;
import com.mentor.club.model.authentication.token.concretes.PasswordResetToken;
import com.mentor.club.model.authentication.token.enums.JwtTokenType;
import com.mentor.club.model.error.HttpCallError;
import com.mentor.club.model.password.ChangeForgottenPasswordRequest;
import com.mentor.club.model.user.User;
import com.mentor.club.repository.IPasswordResetTokenRepository;
import com.mentor.club.repository.IUserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isProvidedPasswordCorrect_checksPasswordCorrectly() {
        String password = "test-password";
        String wrongPassword = "wrong-password";
        String hashedPassword = "$2a$04$/UljyCSLBRCd0cb9H0mCquoR6ZsR68FJe1sXXXzIIwlCAymMyfNZ.";

        assertTrue(passwordService.isProvidedPasswordCorrect(password, hashedPassword));
        assertFalse(passwordService.isProvidedPasswordCorrect(wrongPassword, hashedPassword));
    }

    @Test
    public void hashPassword_hashesPassword() {
        String password = "test-password";
        String hashedPassword = passwordService.hashPassword(password);

        assertTrue(passwordService.isProvidedPasswordCorrect(password, hashedPassword));
    }

    @Test
    public void generateResetForgottenPasswordEmail_ifNoUserIsFound_returnsResponseWithStatusNotFound() {
        String userEmail = "test-email";
        User user = new User();

        user.setEmail(userEmail);

        Optional<User> userWithGivenEmail = Optional.empty();

        when(userRepository.findUserByEmail(userEmail)).thenReturn(userWithGivenEmail);

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void generateResetForgottenPasswordEmail_ifExceptionInThrownInUserRepository_returnsResponseWithStatusInternalServerError() {
        String userEmail = "test-email";

        when(userRepository.findUserByEmail(userEmail)).thenThrow(new InternalException(HttpStatus.BAD_REQUEST, HttpCallError.FAILED_TO_FIND_USER));

        ResponseEntity responseEntity = passwordService.generateResetForgottenPasswordEmail(userEmail);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void generateResetForgottenPasswordEmail_ifExceptionInThrownInPasswordResetTokenRepository_returnsResponseWithStatusInternalServerError() {
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
    public void generateResetForgottenPasswordEmail_ifExceptionInThrownInAwsService_returnsResponseWithStatusInternalServerError() {
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
    public void generateResetForgottenPasswordEmail_ifUserIsFound_returnsResponseWithStatusOK() {
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

    @Test
    public void changeForgottenPassword_ifPasswordResetTokenRepositoryThrowsException_returnsResponseWithStatusNotOK() {
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();

        when(passwordResetTokenRepository.findByToken(any())).thenThrow(new InternalException(HttpStatus.BAD_REQUEST, HttpCallError.FAILED_TO_FIND_TOKEN));

        ResponseEntity responseEntity = passwordService.changeForgottenPassword(changeForgottenPasswordRequest);

        assertNotEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void changeForgottenPassword_ifPasswordResetTokenRepositoryCannotFindToken_returnsResponseWithStatusUnauthorized() {
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();

        when(passwordResetTokenRepository.findByToken(any())).thenReturn(Optional.empty());

        ResponseEntity responseEntity = passwordService.changeForgottenPassword(changeForgottenPasswordRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void changeForgottenPassword_ifPasswordResetTokenIsExpired_returnsResponseWithStatusBadRequest() {
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();
        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);
        passwordResetToken.setExpirationDate(Date.from(Instant.now().minusSeconds(1)));

        when(passwordResetTokenRepository.findByToken(any())).thenReturn(Optional.of(passwordResetToken));

        ResponseEntity responseEntity = passwordService.changeForgottenPassword(changeForgottenPasswordRequest);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void changeForgottenPassword_ifPasswordResetTokenDoesNotHaveUser_returnsResponseWithStatusNotFound() {
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();
        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);
        passwordResetToken.setExpirationDate(Date.from(Instant.now().plusSeconds(1000)));

        when(passwordResetTokenRepository.findByToken(any())).thenReturn(Optional.of(passwordResetToken));

        ResponseEntity responseEntity = passwordService.changeForgottenPassword(changeForgottenPasswordRequest);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void changeForgottenPassword_ifUserCouldNotBeSaved_returnsResponseWithStatusBadRequest() {
        User user = new User();
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();
        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);

        passwordResetToken.setExpirationDate(Date.from(Instant.now().plusSeconds(1000)));
        passwordResetToken.setUser(user);

        when(passwordResetTokenRepository.findByToken(any())).thenReturn(Optional.of(passwordResetToken));
        when(userRepository.save(any())).thenThrow(new InternalException(HttpStatus.INTERNAL_SERVER_ERROR, HttpCallError.FAILED_TO_SAVE_TO_DB));

        ResponseEntity responseEntity = passwordService.changeForgottenPassword(changeForgottenPasswordRequest);

        verify(userRepository, times(1)).save(any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void changeForgottenPassword_ifThereAreNoFailures_changesPasswordAndReturnsResponseWithStatusOK() {
        User user = new User();
        String oldPassword = "test-password";
        String newPassword = "test-new-password";
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();
        PasswordResetToken passwordResetToken = new PasswordResetToken(JwtTokenType.PASSWORD_RESET_TOKEN);

        passwordResetToken.setExpirationDate(Date.from(Instant.now().plusSeconds(1000)));
        user.setHashedPassword(passwordService.hashPassword(oldPassword));
        passwordResetToken.setUser(user);

        changeForgottenPasswordRequest.setNewPassword(newPassword);

        when(passwordResetTokenRepository.findByToken(any())).thenReturn(Optional.of(passwordResetToken));
        when(userRepository.save(any())).thenReturn(user);

        assertTrue(passwordService.isProvidedPasswordCorrect(oldPassword, user.getHashedPassword()));

        ResponseEntity responseEntity = passwordService.changeForgottenPassword(changeForgottenPasswordRequest);

        verify(userRepository, times(1)).save(any());
        verify(userRepository).save(userArgumentCaptor.capture());

        User updatedUser = userArgumentCaptor.getValue();

        assertTrue(passwordService.isProvidedPasswordCorrect(newPassword, updatedUser.getHashedPassword()));
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
