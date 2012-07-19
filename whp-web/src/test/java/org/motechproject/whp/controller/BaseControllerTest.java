package org.motechproject.whp.controller;

import org.junit.Test;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.testing.utils.BaseUnitTest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class BaseControllerTest extends BaseUnitTest {

    protected void setupLoggedInUser(HttpServletRequest request, String username) {
        HttpSession session = mock(HttpSession.class);
        MotechUser loggedInUser = mock(MotechUser.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(loggedInUser);
        when(loggedInUser.getUserName()).thenReturn(username);
    }

    @Test
    public void shouldForwardToErrorPageOnException() throws Exception {
        BaseController controller = new BaseController(){
            @RequestMapping(value = "test", method = RequestMethod.GET)
            public String throwException() {
                if(true)
                    throw new RuntimeException("Error occurred");
                return "success";
            }
        };

        standaloneSetup(controller).build()
                .perform(get("/test"))
                .andExpect(forwardedUrl("errors/error"));
    }

}
