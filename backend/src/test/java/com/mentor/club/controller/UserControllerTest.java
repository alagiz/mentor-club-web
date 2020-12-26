package com.mentor.club.controller;

import com.mentor.club.model.authentication.AuthenticationRequest;
import com.mentor.club.model.password.ChangeForgottenPasswordRequest;
import com.mentor.club.model.password.ChangePasswordRequest;
import com.mentor.club.model.user.NewUser;
import com.mentor.club.service.JwtService;
import com.mentor.club.service.PasswordService;
import com.mentor.club.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private PasswordService passwordService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_Authenticate_CallsAuthenticateOfUserService() {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();

        userController.authenticate(authenticationRequest, httpServletResponse);

        verify(userService, times(1)).authenticate(authenticationRequest, httpServletResponse);
    }

    @Test
    public void test_CreateNewUser_CallsCreateNewUserOfUserService() {
        NewUser newUser = new NewUser();

        userController.createNewUser(newUser);

        verify(userService, times(1)).createNewUser(newUser);
    }

    @Test
    public void test_ConfirmEmail_CallsConfirmEmailOfUserService() {
        UUID emailConfirmTokenId = UUID.randomUUID();

        userController.confirmEmail(emailConfirmTokenId);

        verify(userService, times(1)).confirmEmail(emailConfirmTokenId);
    }

    @Test
    public void test_GenerateResetForgottenPassword_CallsGenerateResetForgottenPasswordOfPasswordService() {
        String userEmail = "test-user-email";

        userController.generateResetForgottenPasswordEmail(userEmail);

        verify(passwordService, times(1)).generateResetForgottenPasswordEmail(userEmail);
    }

    @Test
    public void test_ChangeForgottenPassword_CallsChangeForgottenPasswordOfPasswordService() {
        ChangeForgottenPasswordRequest changeForgottenPasswordRequest = new ChangeForgottenPasswordRequest();

        userController.changeForgottenPassword(changeForgottenPasswordRequest);

        verify(passwordService, times(1)).changeForgottenPassword(changeForgottenPasswordRequest);
    }

    @Test
    public void test_ChangePassword_CallsChangePasswordOfPasswordService() {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        String authorization = "test-authorization";

        userController.changePassword(changePasswordRequest, authorization);

        verify(passwordService, times(1)).changePassword(changePasswordRequest, authorization);
    }

    @Test
    public void test_Logout_CallsLogoutOfUserService() {
        String authorization = "";
        String userName = "";

        userController.logout(authorization, userName);

        verify(userService, times(1)).logout(authorization, userName);
    }
}
