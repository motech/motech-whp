package org.motechproject.whp.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.authentication.LoginSuccessHandler;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.patient.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.repository.AllProviders;

import javax.servlet.http.HttpServletRequest;
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

    @Before
    public void setup() {
        initMocks(this);
        homeController = new HomeController(allProviders);
    }

    @Test
    public void shouldRedirectToTheListPageForProviderUponLogin() {
        Provider provider = ProviderBuilder.startRecording().withDefaults().withId(UUID.randomUUID().toString()).build();
        login(authenticatedUserFor(provider));
        setupProvider(provider);
        assertEquals("redirect:/patients?provider=" + provider.getProviderId(), homeController.homePage(request));
    }

    @Test
    public void shouldRedirectToProvidersListingPageUponItAdminLogin() {
        Provider provider = ProviderBuilder.startRecording().withDefaults().withId(UUID.randomUUID().toString()).build();
        login(authenticatedAdmin(WHPRole.IT_ADMIN));
        setupProvider(provider);
        assertEquals("redirect:/providers/list", homeController.homePage(request));
    }

    @Test
    public void shouldRedirectToTheAllPatientsPageWhenCmfAdminLogsIn() {
        Provider provider = ProviderBuilder.startRecording().withDefaults().withId(UUID.randomUUID().toString()).build();
        login(authenticatedAdmin(WHPRole.CMF_ADMIN));
        setupProvider(provider);
        assertEquals("redirect:/patients/all", homeController.homePage(request));
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

    private MotechUser authenticatedAdmin(WHPRole whpRole) {
        MotechUser authenticatedUser = mock(MotechUser.class);
        when(authenticatedUser.getRoles()).thenReturn(Arrays.asList(whpRole.name()));
        return authenticatedUser;
    }

    private void setupProvider(Provider provider) {
        when(allProviders.get(provider.getId())).thenReturn(provider);
    }
}
