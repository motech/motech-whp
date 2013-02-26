package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.applicationservice.adherence.AdherenceSubmissionService;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.security.authentication.LoginSuccessHandler.LOGGED_IN_USER;
import static org.motechproject.whp.controller.ProviderAdherenceStatusController.*;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ProviderAdherenceStatusControllerTest extends BaseUnitTest {

    private ProviderAdherenceStatusController providerAdherenceStatusController;
    @Mock
    private AdherenceSubmissionService adherenceSubmissionService;

    @Before
    public void setup() {
        initMocks(this);
        providerAdherenceStatusController = new ProviderAdherenceStatusController(adherenceSubmissionService);
    }

    @Test
    public void shouldShowAdherenceStatus() throws Exception {
        List<Provider> providersWithoutAdherence = asList(new ProviderBuilder().withProviderId("providerId1").build());
        List<Provider> providersWithAdherence = asList(new ProviderBuilder().withProviderId("providerId2").build());

        String loggedInDistrict = "Patna";
        LocalDate today = new LocalDate(2012, 12, 3);
        mockCurrentDate(today);

        when(adherenceSubmissionService.providersPendingAdherence(eq(loggedInDistrict), any(LocalDate.class), any(LocalDate.class))).thenReturn(providersWithoutAdherence);
        when(adherenceSubmissionService.providersWithAdherence(eq(loggedInDistrict), any(LocalDate.class), any(LocalDate.class))).thenReturn(providersWithAdherence);

        loginAsDistrict(loggedInDistrict);
        standaloneSetup(providerAdherenceStatusController).build()
                .perform(get("/providers/adherenceStatus/").sessionAttr(LOGGED_IN_USER, loginAsDistrict(loggedInDistrict)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(PROVIDER_LIST_PENDING_ADHERENCE, providersWithoutAdherence))
                .andExpect(model().attribute(PROVIDER_LIST_WITH_ADHERENCE, providersWithAdherence))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_FROM, "26/11/2012"))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_TO, "02/12/2012"))
                .andExpect(view().name("provider/adherence"));
    }

    @Test
    public void shouldPrintAdherenceStatus() throws Exception {
        List<Provider> providersWithoutAdherence = asList(new ProviderBuilder().withProviderId("providerId1").build());
        List<Provider> providersWithAdherence = asList(new ProviderBuilder().withProviderId("providerId2").build());

        String loggedInDistrict = "Patna";
        LocalDate today = new LocalDate(2012, 12, 3);
        mockCurrentDate(today);

        when(adherenceSubmissionService.providersPendingAdherence(eq(loggedInDistrict), any(LocalDate.class), any(LocalDate.class))).thenReturn(providersWithoutAdherence);
        when(adherenceSubmissionService.providersWithAdherence(eq(loggedInDistrict), any(LocalDate.class), any(LocalDate.class))).thenReturn(providersWithAdherence);

        loginAsDistrict(loggedInDistrict);
        standaloneSetup(providerAdherenceStatusController).build()
                .perform(get("/providers/adherenceStatus/print").sessionAttr(LOGGED_IN_USER, loginAsDistrict(loggedInDistrict)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(PROVIDER_LIST_PENDING_ADHERENCE, providersWithoutAdherence))
                .andExpect(model().attribute(PROVIDER_LIST_WITH_ADHERENCE, providersWithAdherence))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_FROM, "26/11/2012"))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_TO, "02/12/2012"))
                .andExpect(view().name("provider/printAdherence"));
    }

    private MotechUser loginAsDistrict(String loggedInDistrict) {
        return new MotechUser(new MotechWebUser(loggedInDistrict, "password", loggedInDistrict, Collections.<String>emptyList()));
    }
}
