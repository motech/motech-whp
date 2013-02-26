package org.motechproject.whp.controller;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.reporting.domain.ProviderAdherenceSummaries;
import org.motechproject.whp.reporting.service.ReportingDataService;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.security.authentication.LoginSuccessHandler.LOGGED_IN_USER;
import static org.motechproject.whp.controller.ProviderAdherenceStatusController.PROVIDED_ADHERENCE_FROM;
import static org.motechproject.whp.controller.ProviderAdherenceStatusController.PROVIDED_ADHERENCE_TO;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ProviderAdherenceStatusControllerTest extends BaseUnitTest {

    private ProviderAdherenceStatusController providerAdherenceStatusController;
    @Mock
    private ReportingDataService reportingDataService;

    @Before
    public void setup() {
        initMocks(this);
        providerAdherenceStatusController = new ProviderAdherenceStatusController(reportingDataService);
    }

    @Test
    public void shouldShowAdherenceStatus() throws Exception {
        String loggedInDistrict = "Patna";
        LocalDate today = new LocalDate(2012, 12, 3);
        mockCurrentDate(today);

        loginAsDistrict(loggedInDistrict);
        ProviderAdherenceSummaries expectedAdherenceSummaries = new ProviderAdherenceSummaries();
        when(reportingDataService.getProviderAdherenceStatus(loggedInDistrict)).thenReturn(expectedAdherenceSummaries);

        standaloneSetup(providerAdherenceStatusController).build()
                .perform(get("/providers/adherenceStatus/").sessionAttr(LOGGED_IN_USER, loginAsDistrict(loggedInDistrict)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ProviderAdherenceStatusController.PROVIDER_ADHERENCE_STATUSES, expectedAdherenceSummaries))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_FROM, "26/11/2012"))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_TO, "02/12/2012"))
                .andExpect(view().name("provider/adherence"));
    }

    @Test
    public void shouldPrintAdherenceStatus() throws Exception {
        String loggedInDistrict = "Patna";
        LocalDate today = new LocalDate(2012, 12, 3);
        mockCurrentDate(today);

        loginAsDistrict(loggedInDistrict);
        ProviderAdherenceSummaries expectedAdherenceSummaries = new ProviderAdherenceSummaries();
        when(reportingDataService.getProviderAdherenceStatus(loggedInDistrict)).thenReturn(expectedAdherenceSummaries);

        standaloneSetup(providerAdherenceStatusController).build()
                .perform(get("/providers/adherenceStatus/print").sessionAttr(LOGGED_IN_USER, loginAsDistrict(loggedInDistrict)))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ProviderAdherenceStatusController.PROVIDER_ADHERENCE_STATUSES, expectedAdherenceSummaries))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_FROM, "26/11/2012"))
                .andExpect(model().attribute(PROVIDED_ADHERENCE_TO, "02/12/2012"))
                .andExpect(view().name("provider/printAdherence"));
    }

    private MotechUser loginAsDistrict(String loggedInDistrict) {
        return new MotechUser(new MotechWebUser(loggedInDistrict, "password", loggedInDistrict, Collections.<String>emptyList()));
    }
}
