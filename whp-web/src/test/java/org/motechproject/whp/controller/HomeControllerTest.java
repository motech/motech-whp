package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.domain.HomePage;
import org.motechproject.whp.service.HomePageService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class HomeControllerTest {

    HomeController homeController;

    @Mock
    AllProviders allProviders;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Mock
    HomePageService homePageService;

    @Before
    public void setup() {
        initMocks(this);
        initializeHomePageService();
        homeController = new HomeController(allProviders, homePageService);
    }

    private void initializeHomePageService() {
        when(homePageService.homePageFor(asList(WHPRole.CMF_ADMIN.name()))).thenReturn(new HomePage(asList("/patients/list")));
        when(homePageService.homePageFor(asList(WHPRole.IT_ADMIN.name()))).thenReturn(new HomePage(asList("/providers/list")));
        when(homePageService.homePageFor(asList(WHPRole.FIELD_STAFF.name()))).thenReturn(new HomePage(asList("/providers/adherenceStatus")));
        when(homePageService.homePageFor(asList(WHPRole.PROVIDER.name()))).thenReturn(new HomePage());
    }

    @Test
    public void shouldRedirectToTheListPageForProviderUponLogin() {
        Provider provider = ProviderBuilder.newProviderBuilder().withDefaults().withId(UUID.randomUUID().toString()).build();
        login(authenticatedUserFor(provider));
        setupProvider(provider);
        assertEquals("redirect:/patients/listByProvider", homeController.homePage(request,response));
    }

    @Test
    public void shouldRedirectToProvidersListingPageUponItAdminLogin() {
        Provider provider = ProviderBuilder.newProviderBuilder().withDefaults().withId(UUID.randomUUID().toString()).build();
        login(authenticatedUser(WHPRole.IT_ADMIN));
        setupProvider(provider);
        assertEquals("redirect:/providers/list", homeController.homePage(request, response));
    }

    @Test
    public void shouldRedirectToTheAllPatientsPageWhenCmfAdminLogsIn() {
        Provider provider = ProviderBuilder.newProviderBuilder().withDefaults().withId(UUID.randomUUID().toString()).build();
        login(authenticatedUser(WHPRole.CMF_ADMIN));
        setupProvider(provider);
        assertEquals("redirect:/patients/list", homeController.homePage(request, response));
    }

    @Test
    public void shouldRedirectToProvidersPendingAdherencePageForFieldStaff() {
        login(authenticatedUser(WHPRole.FIELD_STAFF));
        assertEquals("redirect:/providers/adherenceStatus", homeController.homePage(request, response));
    }

    private void login(MotechUser authenticatedUser) {
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(LoginSuccessHandler.LOGGED_IN_USER)).thenReturn(authenticatedUser);
    }

    private MotechUser authenticatedUserFor(Provider provider) {
        MotechUser authenticatedUser = mock(MotechUser.class);
        when(authenticatedUser.getExternalId()).thenReturn(provider.getId());
        when(authenticatedUser.getRoles()).thenReturn(asList(WHPRole.PROVIDER.name()));
        return authenticatedUser;
    }

    private MotechUser authenticatedUser(WHPRole whpRole) {
        MotechUser authenticatedUser = mock(MotechUser.class);
        when(authenticatedUser.getRoles()).thenReturn(Arrays.asList(whpRole.name()));
        return authenticatedUser;
    }

    private void setupProvider(Provider provider) {
        when(allProviders.get(provider.getId())).thenReturn(provider);
    }
}
