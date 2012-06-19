package org.motechproject.whp.controller;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.refdata.repository.AllCmfLocations;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.uimodel.ProviderRow;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    private MotechAuthenticationService motechAuthenticationService;
    @Mock
    private AllProviders allProviders;
    @Mock
    private AllCmfLocations allDistricts;

    Provider provider1 = new Provider("aa", "9845678761", "district",
            DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    Provider provider2 = new Provider("ab", "9845678761", "district",
            DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    Provider provider3 = new Provider("ac", "9845678761", "district",
            DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

    private ProviderController providerController;
    List<Provider> testProviders = new ArrayList<Provider>();

    @Before
    public void setup() {
        initMocks(this);
        testProviders.add(provider1);
        testProviders.add(provider2);
        testProviders.add(provider3);
        when(allProviders.list()).thenReturn(testProviders);
        when(allProviders.findByProviderId("aa")).thenReturn(provider1);
        when(allProviders.findByProviderId("ab")).thenReturn(provider2);
        when(allProviders.findByProviderId("ac")).thenReturn(provider3);

        MotechUser user1 = mock(MotechUser.class);
        when(user1.getUserName()).thenReturn(provider1.getProviderId());
        MotechUser user2 = mock(MotechUser.class);
        when(user1.isActive()).thenReturn(true);

        when(user2.getUserName()).thenReturn(provider2.getProviderId());
        when(user2.isActive()).thenReturn(true);

        MotechUser user3 = mock(MotechUser.class);
        when(user3.getUserName()).thenReturn(provider3.getProviderId());
        when(user3.isActive()).thenReturn(true);

        List<MotechUser> motechUsers = asList(user1,user2,user3);

        when(motechAuthenticationService.findByRole(anyString())).thenReturn(motechUsers);
        providerController = new ProviderController(allProviders, allDistricts, motechAuthenticationService);
    }

    @Test
    public void shouldLoadProviderSearchPage_verifyViewMappingForGET() throws Exception {
        String viewName = providerController.loadProviderSearchPage(uiModel);
        assertEquals("provider/list", viewName);
    }

    @Test
    public void shouldLoadProviderSearchPage_verifyViewMappingForPOST() throws Exception {
        String viewName = providerController.searchMatchingProviders("providerId", uiModel);
        assertEquals("provider/list", viewName);
    }

    @Test
    public void shouldLoadProviderSearchPage_withAllProvidersByDefault() throws Exception {
        providerController.loadProviderSearchPage(uiModel);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(wrapIntoProviderRows(testProviders)));
    }

    private List<ProviderRow> wrapIntoProviderRows(List<Provider> providers) {
        List<ProviderRow> providerRows = new ArrayList<ProviderRow>();
        for(Provider provider : providers) {
            providerRows.add(new ProviderRow(provider, true));
        }
        return providerRows;
    }

    @Test
    public void shouldListAllProviders_whenSearchedByNullOrEmptyStringAsProviderId() throws Exception {
        providerController.searchMatchingProviders(null, uiModel);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(wrapIntoProviderRows(testProviders)));
    }

    @Test
    public void shouldListAllProviders_whenSearchedByEmptyStringAsProviderId() throws Exception {
        providerController.searchMatchingProviders("", uiModel);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(wrapIntoProviderRows(testProviders)));
    }

    @Test
    public void shouldListMatchingProvider_whenSearchedByValidProviderId() throws Exception {
        providerController.searchMatchingProviders("ab", uiModel);
        List<Provider> matchingProviders = new ArrayList<Provider>();
        matchingProviders.add(provider2);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(wrapIntoProviderRows(matchingProviders)));
    }

    @Test
    public void shouldNotListAnything_whenSearchedByInvalidProviderId() throws Exception {
        providerController.searchMatchingProviders("doesNotExist", uiModel);
        List<ProviderRow> matchingProviders = new ArrayList<ProviderRow>();
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(matchingProviders));
    }
}
