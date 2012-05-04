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

    @Before
    public void setup() {
        initMocks(this);
        userController = new UserController(motechAuthenticationService);
    }

    @Test
    public void shouldChangePassword() {
        login(authenticatedAdmin());

        String responseBody = userController.changePassword("newPassword", request);
        assertEquals("", responseBody);

        verify(motechAuthenticationService).changePassword("admin", "newPassword");
    }

    private void login(AuthenticatedUser authenticatedUser) {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(authenticatedUser);
    }

    private AuthenticatedUser authenticatedAdmin() {
        AuthenticatedUser authenticatedUser = mock(AuthenticatedUser.class);
        when(authenticatedUser.getUsername()).thenReturn("admin");
        when(authenticatedUser.getRoles()).thenReturn(new Roles(Arrays.asList(new Role(WHPRole.ADMIN.name()))));
        return authenticatedUser;
    }
}
