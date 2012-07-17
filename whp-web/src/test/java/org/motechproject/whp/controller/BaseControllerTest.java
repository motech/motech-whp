package org.motechproject.whp.controller;

import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.testing.utils.BaseUnitTest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseControllerTest extends BaseUnitTest {

    protected void setupLoggedInUser(HttpServletRequest request, String username) {
        HttpSession session = mock(HttpSession.class);
        MotechUser loggedInUser = mock(MotechUser.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(loggedInUser);
        when(loggedInUser.getUserName()).thenReturn(username);
    }

}
