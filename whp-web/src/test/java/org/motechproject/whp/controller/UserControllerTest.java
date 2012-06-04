package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.LoginSuccessHandler;
import org.motechproject.security.domain.AuthenticatedUser;
import org.motechproject.security.domain.Role;
import org.motechproject.security.domain.Roles;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.refdata.domain.WHPRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserControllerTest {

    private UserController userController;

    @Mock
    private MotechAuthenticationService motechAuthenticationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    @Before
    public void setup() {
        initMocks(this);
        userController = new UserController(motechAuthenticationService);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void shouldChangePassword() {
        AuthenticatedUser authenticatedUser = authenticatedAdmin();
        login(authenticatedUser);

        when(motechAuthenticationService.changePassword("admin", "newPassword")).thenReturn(authenticatedUser);

        String responseBody = userController.changePassword("newPassword", request);

        assertEquals("", responseBody);
        verify(session).setAttribute(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser);
    }

    private void login(AuthenticatedUser authenticatedUser) {
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(authenticatedUser);
    }

    private AuthenticatedUser authenticatedAdmin() {
        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        when(authenticatedUser.getUsername()).thenReturn("admin");
        when(authenticatedUser.getRoles()).thenReturn(new Roles(Arrays.asList(new Role(WHPRole.CMF_ADMIN.name()))));
        return authenticatedUser;
    }
}
