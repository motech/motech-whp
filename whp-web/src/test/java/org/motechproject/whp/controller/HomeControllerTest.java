package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.domain.HomePage;
import org.motechproject.whp.service.HomePageService;
import org.motechproject.whp.user.domain.WHPRole;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class HomeControllerTest {

    HomeController homeController;

    @Mock
    HomePageService homePageService;

    @Before
    public void setup() {
        initMocks(this);
        initializeHomePageService();
        homeController = new HomeController(homePageService);
    }

    private void initializeHomePageService() {
        when(homePageService.homePageFor(asList(WHPRole.CMF_ADMIN.name()))).thenReturn(new HomePage(asList("/patients/list")));
        when(homePageService.homePageFor(asList(WHPRole.IT_ADMIN.name()))).thenReturn(new HomePage(asList("/providers/list")));
        when(homePageService.homePageFor(asList(WHPRole.FIELD_STAFF.name()))).thenReturn(new HomePage(asList("/providers/adherenceStatus")));
        when(homePageService.homePageFor(asList(WHPRole.PROVIDER.name()))).thenReturn(new HomePage());
    }

    @Test
    public void shouldRedirectToTheListPageForProviderUponLogin() throws Exception {
        standaloneSetup(homeController)
                .build()
                .perform(get("/")
                .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser(WHPRole.PROVIDER)))
                .andExpect(status().isOk())
        .andExpect(redirectedUrl("/patients/listByProvider"));
    }

    @Test
    public void shouldRedirectToProvidersListingPageUponItAdminLogin() throws Exception {
        standaloneSetup(homeController)
                .build()
                .perform(get("/")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser(WHPRole.IT_ADMIN)))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/providers/list"));
    }

    @Test
    public void shouldRedirectToTheAllPatientsPageWhenCmfAdminLogsIn() throws Exception {
        standaloneSetup(homeController)
                .build()
                .perform(get("/")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser(WHPRole.CMF_ADMIN)))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/patients/list"));
    }

    @Test
    public void shouldRedirectToProvidersPendingAdherencePageForFieldStaff() throws Exception {
        standaloneSetup(homeController)
                .build()
                .perform(get("/")
                        .sessionAttr(LoginSuccessHandler.LOGGED_IN_USER, authenticatedUser(WHPRole.FIELD_STAFF)))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/providers/adherenceStatus"));
    }

    private MotechUser authenticatedUser(WHPRole whpRole) {
        MotechUser authenticatedUser = mock(MotechUser.class);
        when(authenticatedUser.getRoles()).thenReturn(Arrays.asList(whpRole.name()));
        return authenticatedUser;
    }
}
