package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.whp.refdata.domain.WHPRole;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
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
        MotechWebUser authenticatedUser = authenticatedAdmin();
        login(authenticatedUser);

        when(motechAuthenticationService.changePassword("admin", "newPassword")).thenReturn(authenticatedUser);

        String responseBody = userController.changePassword("newPassword", request);

        assertEquals("", responseBody);
        verify(session).setAttribute(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser);
    }

    private void login(MotechWebUser authenticatedUser) {
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(authenticatedUser);
    }

    private MotechWebUser authenticatedAdmin() {
        MotechWebUser authenticatedUser = mock(MotechWebUser.class);
        when(authenticatedUser.getUserName()).thenReturn("admin");
        when(authenticatedUser.getRoles()).thenReturn(asList(WHPRole.CMF_ADMIN.name()));
        return authenticatedUser;
    }
}
