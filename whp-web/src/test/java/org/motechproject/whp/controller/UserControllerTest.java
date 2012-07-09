package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private UserService userService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Before
    public void setup() {
        initMocks(this);
        userController = new UserController(userService);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void shouldChangePassword() {
        MotechUser authenticatedUser = authenticatedAdmin();
        login(authenticatedUser);

        when(userService.changePassword("admin","oldPassword", "newPassword")).thenReturn(authenticatedUser);

        String responseBody = userController.changePassword("oldPassword","newPassword", request);

        assertEquals("", responseBody);
        verify(session).setAttribute(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser);
    }

    @Test
    public void shouldActivateAndResetPassword() {
        String providerId = "providerId";
        String password = "password";

        when(userService.resetPassword(providerId, password)).thenReturn(true);

        assertEquals("", userController.activateUser(providerId, password));
        verify(userService,times(1)).resetPassword(providerId,password);
        verify(userService,times(1)).activateUser(providerId);
    }

    @Test
    public void shouldReturnErrorMessageIfActivationFails(){
        String providerId = "providerId";
        String password = "password";

        when(userService.resetPassword(providerId, password)).thenReturn(false);

        assertEquals("User Does not exist",userController.activateUser(providerId,password));
        verify(userService,times(1)).resetPassword(providerId,password);
        verify(userService,never()).activateUser(anyString());
    }

    private void login(MotechUser authenticatedUser) {
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(authenticatedUser);
    }

    private MotechUser authenticatedAdmin() {
        MotechUser authenticatedUser = mock(MotechUser.class);
        when(authenticatedUser.getUserName()).thenReturn("admin");
        when(authenticatedUser.getRoles()).thenReturn(asList(WHPRole.CMF_ADMIN.name()));
        return authenticatedUser;
    }
}
