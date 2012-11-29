package org.motechproject.whp.controller;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Collections.emptyList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

public class ProviderControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    private ProviderService providerService;
    @Mock
    private AllDistricts allDistricts;

    Provider provider1 = new Provider("aa", "9845678761", "districtA",
            DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    Provider provider2 = new Provider("ab", "9845678761", "districtB",
            DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    Provider provider3 = new Provider("ac", "9845678761", "districtA",
            DateTimeFormat.forPattern(DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

    private ProviderController providerController;

    @Before
    public void setup() {
        initMocks(this);

        MotechUser user1 = mock(MotechUser.class);
        when(user1.getUserName()).thenReturn(provider1.getProviderId());
        MotechUser user2 = mock(MotechUser.class);
        when(user1.isActive()).thenReturn(true);

        when(user2.getUserName()).thenReturn(provider2.getProviderId());
        when(user2.isActive()).thenReturn(true);

        MotechUser user3 = mock(MotechUser.class);
        when(user3.getUserName()).thenReturn(provider3.getProviderId());
        when(user3.isActive()).thenReturn(true);

        Map<String, MotechUser> motechUsers = new HashMap();
        motechUsers.put(user1.getUserName(), user1);
        motechUsers.put(user2.getUserName(), user2);
        motechUsers.put(user3.getUserName(), user3);

        when(providerService.fetchAllWebUsers()).thenReturn(motechUsers);

        ArrayList<District> districts = new ArrayList<>();
        districts.add(new District("districtA"));
        districts.add(new District("districtB"));
        when(allDistricts.getAll()).thenReturn(districts);

        providerController = new ProviderController(providerService, allDistricts);
    }

    @Test
    public void shouldLoadProviderSearchPage_verifyViewMappingForGET() throws Exception {
        String viewName = providerController.loadProviderSearchPage(uiModel, request);
        assertEquals("provider/list", viewName);
    }

    @Test
    public void shouldLoadProviderSearchPage_withProvidersByFirstDistrict() throws Exception {

        List<District> expectedList = Arrays.asList(new District("d1"), new District("d2"));
        when(allDistricts.getAll()).thenReturn(expectedList);

        providerController.loadProviderSearchPage(uiModel, request);
        verify(uiModel).addAttribute(eq(providerController.DISTRICT_LIST), eq(expectedList));
        verify(uiModel).addAttribute(eq(providerController.SELECTED_DISTRICT), eq("d1"));
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_ID), eq(""));
    }

    @Test
    public void shouldShowEmptyPageForProvidersPendingAdherence() throws Exception {
        standaloneSetup(providerController).build()
                .perform(get("/providers/pendingAdherence/"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void shouldFetchAllProvidersForDistrict() throws Exception {
        List<Provider> providers = emptyList();
        when(providerService.fetchBy("Begusarai")).thenReturn(providers);

        standaloneSetup(providerController).build()
                .perform(get("/providers/byDistrict/Begusarai"))
                .andExpect(status().isOk())
                .andExpect(model().size(1))
                .andExpect(model().attribute("providerList", providers))
                .andExpect(view().name("provider/listByDistrict"));
    }
}
