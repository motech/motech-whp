package org.motechproject.whp.user.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.scheduler.context.EventContext;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechAuthenticationService;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.contract.ProviderRequest;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.domain.ProviderIds;
import org.motechproject.whp.user.domain.WHPRole;
import org.motechproject.whp.user.mapper.ProviderReportingService;
import org.motechproject.whp.user.repository.AllProviders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.user.builder.ProviderBuilder.newProviderBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ProviderServiceTest {

    @Mock
    private MotechAuthenticationService motechAuthenticationService;

    @Mock
    private AllProviders allProviders;

    @Mock
    private EventContext eventContext;

    @Mock
    private AllDistricts allDistricts;

    @Mock
    private ProviderReportingService providerReportingService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private ProviderService providerService;
    Integer startIndex;
    Integer rowsPerPage;
    String providerId = "providerId";

    @Before
    public void setUp() {
        initMocks(this);
        providerService = new ProviderService(motechAuthenticationService, allProviders, eventContext, providerReportingService);
        startIndex = 0;
        rowsPerPage = 2;
    }

    @Test
    public void shouldFetchProviderByDistrictAndProviderId() {
        Provider expectedProvider = mock(Provider.class);
        when(allProviders.findByProviderId(providerId)).thenReturn(expectedProvider);

        List<Provider> providers = providerService.fetchByFilterParams(startIndex, rowsPerPage, "district", "providerId");

        assertThat(providers, hasItem(expectedProvider));
        assertThat(providers.size(), is(1));
        verify(allProviders).findByProviderId("providerId");
    }

    @Test
    public void shouldHandleNoResultForFilterByProviderId() {
        when(allProviders.findByProviderId(providerId)).thenReturn(null);

        List<Provider> providers = providerService.fetchByFilterParams(startIndex, rowsPerPage, "district", providerId);

        assertTrue(providers.isEmpty());
        verify(allProviders).findByProviderId(providerId);
    }

    @Test
    public void shouldFetchProvidersByOnlyDistrict_WhenProviderIdIsEmpty() {
        providerService.fetchByFilterParams(startIndex, rowsPerPage, "district", "");
        verify(allProviders).paginateByDistrict(startIndex, rowsPerPage, "district");
    }

    @Test
    public void shouldNotReturnProvidersForNoCriteria(){
        providerService.fetchByFilterParams(startIndex, rowsPerPage, "", "");
        verifyNoMoreInteractions(allProviders);
    }

    @Test
    public void shouldFetchProviderByProviderId(){
        providerService.fetchByFilterParams(startIndex, rowsPerPage, "", "providerId");
        verify(allProviders).findByProviderId("providerId");
    }

    @Test
    public void shouldReturnProviderIdsByDistrict() {
        String district = "district";
        String providerId = "1234";

        List<Provider> providersBelongingToDistrict = asList(new ProviderBuilder().withDefaults().withDistrict(district).withProviderId(providerId).build());
        ProviderIds providerIdsBelongingToDistrict = new ProviderIds(asList(providerId));

        when(allProviders.findByDistrict(district)).thenReturn(providersBelongingToDistrict);
        assertEquals(providerIdsBelongingToDistrict, providerService.findByDistrict(district));
        verify(allProviders).findByDistrict(district);
    }

    @Test
    public void shouldFetchAllProviderWebUsers() {
        MotechUser motechUser = new MotechUser(new MotechWebUser("provider", "password", "externalId", null));
        when(motechAuthenticationService.findByRole(WHPRole.PROVIDER.name())).thenReturn(Arrays.asList(motechUser));

        Map<String, MotechUser> motechUserMap = providerService.fetchAllWebUsers();

        verify(motechAuthenticationService).findByRole(WHPRole.PROVIDER.name());
        assertNotNull(motechUserMap);
        assertEquals(1, motechUserMap.size());
        assertTrue(motechUserMap.containsKey("provider"));
        assertEquals(motechUser, motechUserMap.get("provider"));
    }

    @Test
    public void shouldFetchProviderById() {
        String providerId = "providerId";
        Provider expectedProvider = new Provider();
        when(allProviders.findByProviderId(providerId)).thenReturn(expectedProvider);
        Provider provider = providerService.findByProviderId(providerId);
        assertEquals(expectedProvider, provider);
        verify(allProviders).findByProviderId(providerId);
    }

    @Test
    public void shouldReturnTrueIfProviderExists_forGivenMobileNumber() {
        String mobileNumber = "1234567890";
        Provider provider = newProviderBuilder().withDefaults().withPrimaryMobileNumber(mobileNumber).build();
        when(allProviders.findByMobileNumber(mobileNumber)).thenReturn(provider);

        Provider returnedProvider = providerService.findByMobileNumber(mobileNumber);

        assertThat(returnedProvider, is(provider));
        verify(allProviders).findByMobileNumber(mobileNumber);
    }

    @Test
    public void shouldCreateProvider() {
        String providerId = "providerId";
        ProviderRequest providerRequest = new ProviderRequest(providerId, "district", "primaryMobile", now());
        when(allProviders.findByProviderId(providerId)).thenReturn(null);

        providerService.createOrUpdateProvider(providerRequest);

        verify(allProviders).findByProviderId(providerRequest.getProviderId());
        Provider provider = providerRequest.makeProvider();
        verify(allProviders).addOrReplace(provider);
        verify(providerReportingService).reportProvider(provider);
    }

    @Test
    public void shouldUpdateProvider() {
        String providerId = "providerId";
        ProviderRequest providerRequest = new ProviderRequest(providerId, "district", "primaryMobile", now());
        Provider provider = providerRequest.makeProvider();
        Provider providerFromDatabase = providerRequest.makeProvider();

        when(allProviders.findByProviderId(providerRequest.getProviderId())).thenReturn(providerFromDatabase);

        providerService.createOrUpdateProvider(providerRequest);

        verify(allProviders).findByProviderId(providerRequest.getProviderId());
        verify(allProviders).addOrReplace(provider);
        verify(providerReportingService).reportProvider(provider);
    }

    @Test
    public void shouldUpdateProviderAndSendEvent_whenDistrictHasChanged() {
        String providerId = "providerId";
        ProviderRequest providerRequest = new ProviderRequest(providerId, "district", "primaryMobile", now());
        Provider provider = providerRequest.makeProvider();
        Provider providerFromDatabase = providerRequest.makeProvider();
        providerFromDatabase.setDistrict("oldDistrict");

        when(allProviders.findByProviderId(providerRequest.getProviderId())).thenReturn(providerFromDatabase);

        providerService.createOrUpdateProvider(providerRequest);

        verify(eventContext).send(EventKeys.PROVIDER_DISTRICT_CHANGE, provider.getProviderId());
        verify(allProviders).findByProviderId(providerRequest.getProviderId());
        verify(allProviders).addOrReplace(provider);
    }


    @Test
    public void shouldReturnFalseIfProviderDoesNotExists_forGivenMobileNumber() {
        String mobileNumber = "1234567899";
        when(allProviders.findByMobileNumber(mobileNumber)).thenReturn(null);

        Provider returnedProvider = providerService.findByMobileNumber(mobileNumber);

        verify(allProviders).findByMobileNumber(mobileNumber);
        assertThat(returnedProvider, nullValue());
    }

    @Test
    public void shouldFetchAllProvidersGivenProviderIds(){
        ProviderIds providerIds = new ProviderIds(asList("1234", "5678"));
        
        ArrayList<Provider> providers = new ArrayList<>();
        when(allProviders.findByProviderIds(providerIds)).thenReturn(providers);

        List<Provider> actualProviders = providerService.findByProviderIds(providerIds);

        assertEquals(providers, actualProviders);
        verify(allProviders).findByProviderIds(providerIds);
    }

    @Test
    public void shouldGetAllProviders() {
        List expectedProviders = mock(List.class);
        when(allProviders.getAll()).thenReturn(expectedProviders);

        List<Provider> providers = providerService.getAll();

        assertEquals(expectedProviders, providers);
        verify(allProviders).getAll();
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(allProviders);
        verifyNoMoreInteractions(eventContext);
        verifyNoMoreInteractions(motechAuthenticationService);
    }

    @Test
    public void shouldReturnPaginatedCountIfFilterByDistrict(){
        FilterParams filterParams = new FilterParams();
        String district = "d1";
        filterParams.put("district", district);
        filterParams.put("providerId", "");

        when(allProviders.findCountByDistrict(district)).thenReturn("4");

        assertThat(providerService.count(filterParams), is(4));

        verify(allProviders).findCountByDistrict(filterParams.get("district").toString());

    }

    @Test
    public void shouldReturnOneIfFilterByProviderIdAndOrDistrictReturnProvider(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("district", "");
        filterParams.put("providerId", "p1");
        when(allProviders.findByProviderId("p1")).thenReturn(new Provider());

        assertThat(providerService.count(filterParams), is(1));

        verify(allProviders).findByProviderId("p1");
    }

    @Test
    public void shouldReturnZeroIfFilterByProviderIdAndOrDistrictDoesNotReturnProvider(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("district", "");
        filterParams.put("providerId", "p1");
        when(allProviders.findByProviderId("p1")).thenReturn(null);

        assertThat(providerService.count(filterParams), is(0));
        verify(allProviders).findByProviderId("p1");

    }

    @Test
    public void shouldReturnZeroIfFilterByNothing(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("district", "");
        filterParams.put("providerId", "");

        assertThat(providerService.count(filterParams), is(0));

    }

    @Test
    public void shouldReturnZeroIfFilterIsLoadedWithNoFilterKeys(){
        FilterParams filterParams = new FilterParams();

        assertThat(providerService.count(filterParams), is(0));
    }
}
