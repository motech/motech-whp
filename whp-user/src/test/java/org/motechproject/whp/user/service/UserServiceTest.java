package org.motechproject.whp.user.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.service.MotechAuthenticationService;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserServiceTest {
    @Mock
    MotechAuthenticationService motechAuthenticationService;

    UserService userService;

    @Before
    public void setUp(){
        initMocks(this);
        userService = new UserService(motechAuthenticationService);
    }
    @Test
    public void shouldResetPassword(){
        String providerId = "userName";
        String password = "password";

        when(motechAuthenticationService.resetPassword(providerId,password)).thenReturn(true);

        assertTrue(userService.resetPassword(providerId, password));
        verify(motechAuthenticationService,times(1)).resetPassword(providerId,password);
    }

}
