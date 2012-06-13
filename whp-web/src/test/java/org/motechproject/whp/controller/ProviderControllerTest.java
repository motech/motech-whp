package org.motechproject.whp.controller;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.repository.AllMotechWebUsers;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllCmfLocations;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.WHPRole;
import org.motechproject.whp.uimodel.ProviderRow;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
    @Mock
    private AllMotechWebUsers allMotechWebUsers;
    @Mock
    private AllProviders allProviders;
    @Mock
    private AllCmfLocations allDistricts;

    Provider providerAa = new Provider("aa", "9845678761", "district",
            DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    Provider providerAb = new Provider("ab", "9845678761", "district",
            DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));
    Provider providerAc = new Provider("ac", "9845678761", "district",
            DateTimeFormat.forPattern(WHPConstants.DATE_TIME_FORMAT).parseDateTime("12/01/2012 10:10:10"));

    MotechWebUser providerWebUserAa;
    MotechWebUser providerWebUserAb;
    MotechWebUser providerWebUserAc;

    private ProviderController providerController;
    List<Provider> testProviders = new ArrayList<Provider>();

    @Before
    public void setup() {
        initMocks(this);
        testProviders.add(providerAa);
        testProviders.add(providerAb);
        testProviders.add(providerAc);
        when(allProviders.list()).thenReturn(testProviders);
        when(allProviders.findByProviderId("aa")).thenReturn(providerAa);
        when(allProviders.findByProviderId("ab")).thenReturn(providerAb);
        when(allProviders.findByProviderId("ac")).thenReturn(providerAc);

        providerAa.setId("aa");
        providerAb.setId("ab");
        providerAc.setId("ac");
        providerWebUserAa = new MotechWebUser("aa", "password", providerAa.getId(), asList(WHPRole.PROVIDER.name()));
        providerWebUserAb = new MotechWebUser("ab", "password", providerAb.getId(), asList(WHPRole.PROVIDER.name()));
        providerWebUserAc = new MotechWebUser("ac", "password", providerAc.getId(), asList(WHPRole.PROVIDER.name()));
        providerWebUserAa.setActive(true);
        when(allMotechWebUsers.findByRole(anyString())).thenReturn(asList(new MotechWebUser[]{providerWebUserAa, providerWebUserAb, providerWebUserAc}));
        providerWebUserAb.setActive(true);
        providerWebUserAc.setActive(true);
        providerController = new ProviderController(allProviders, allDistricts, allMotechWebUsers);
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
        matchingProviders.add(providerAb);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(wrapIntoProviderRows(matchingProviders)));
    }

    @Test
    public void shouldNotListAnything_whenSearchedByInvalidProviderId() throws Exception {
        providerController.searchMatchingProviders("doesNotExist", uiModel);
        List<ProviderRow> matchingProviders = new ArrayList<ProviderRow>();
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(matchingProviders));
    }
}
