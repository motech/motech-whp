package org.motechproject.whp.controller;

import org.joda.time.format.DateTimeFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllCmfLocations;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderControllerTest {

    @Mock
    Model uiModel;
    @Mock
    HttpServletRequest request;
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
        providerController = new ProviderController(allProviders, allDistricts);
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
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(testProviders));
    }

    @Test
    public void shouldListAllProviders_whenSearchedByNullOrEmptyStringAsProviderId() throws Exception {
        providerController.searchMatchingProviders(null, uiModel);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(testProviders));
    }

    @Test
    public void shouldListAllProviders_whenSearchedByEmptyStringAsProviderId() throws Exception {
        providerController.searchMatchingProviders("", uiModel);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(testProviders));
    }

    @Test
    public void shouldListMatchingProvider_whenSearchedByValidProviderId() throws Exception {
        providerController.searchMatchingProviders("ab", uiModel);
        List<Provider> matchingProviders = new ArrayList<Provider>();
        matchingProviders.add(providerAb);
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(matchingProviders));
    }

    @Test
    public void shouldNotListAnything_whenSearchedByInvalidProviderId() throws Exception {
        providerController.searchMatchingProviders("doesNotExist", uiModel);
        List<Provider> matchingProviders = new ArrayList<Provider>();
        verify(uiModel).addAttribute(eq(providerController.PROVIDER_LIST), eq(matchingProviders));
    }
}
