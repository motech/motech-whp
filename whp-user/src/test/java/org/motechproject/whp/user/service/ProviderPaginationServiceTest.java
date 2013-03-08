package org.motechproject.whp.user.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.paginator.response.PageResults;
import org.motechproject.security.domain.MotechWebUser;
import org.motechproject.security.service.MotechUser;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.uimodel.ProviderRow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderPaginationServiceTest {


    FilterParams filterParams;

    int pageNumber = 1;
    int rowsPerPage = 2;
    private ProviderPaginationService providerPaginationService;

    @Mock
    private ProviderService providerService;
    @Mock
    private AllProviders allProviders;

    @Before
    public void setUp(){
        initMocks(this);
        filterParams = new FilterParams();
        providerPaginationService = new ProviderPaginationService(allProviders, providerService);
    }

    @Test
    public void shouldFetchProvidersByDistrictAccordingToPageNumbers(){
        String district = "d1";
        Provider provider1 = new ProviderBuilder().withProviderId("p1").withDistrict(district).build();
        Provider provider2 = new ProviderBuilder().withProviderId("p2").withDistrict(district).build();

        filterParams.put("selectedDistrict", district);
        filterParams.put("selectedProvider", "");
        rowsPerPage = 1;

        int startIndex = 0;

        MotechUser motechUser = new MotechUser(new MotechWebUser("p1", "password", "externalId", null));
        Map<String, MotechUser> userMap = new HashMap<>();
        userMap.put(motechUser.getUserName(), motechUser);

        when(providerService.fetchByFilterParams(startIndex, rowsPerPage, district, "")).thenReturn(asList(provider1));
        when(providerService.count(filterParams)).thenReturn(2);
        when(providerService.fetchAllWebUsers()).thenReturn(userMap);

        PageResults<ProviderRow> pageResults = providerPaginationService.page(pageNumber, rowsPerPage, filterParams, new SortParams());

        assertThat(pageResults.getTotalRows(), is(2));
        assertThat(pageResults.getPageNo(), is(pageNumber));
        assertThat(pageResults.getResults().size(), is(1));
        assertThat(pageResults.getResults().get(0).getProviderId(), is("p1"));
        assertThat(pageResults.getResults().get(0).getDistrict(), is(district));


        verify(providerService).count(filterParams);
        verify(providerService).fetchByFilterParams(startIndex, rowsPerPage, district, "");
    }

    @Test
    public void shouldFetchProviderForAProviderId(){
        String district = "d1";
        String providerId = "p1";
        Provider provider = new ProviderBuilder().withProviderId(providerId).withDistrict(district).build();

        filterParams.put("selectedDistrict", district);
        filterParams.put("selectedProvider", providerId);
        rowsPerPage = 1;

        int startIndex = 0;

        MotechUser motechUser = new MotechUser(new MotechWebUser("p1", "password", "externalId", null));
        Map<String, MotechUser> userMap = new HashMap<>();
        userMap.put(motechUser.getUserName(), motechUser);

        when(providerService.fetchByFilterParams(startIndex, rowsPerPage, district, providerId)).thenReturn(asList(provider));
        when(providerService.count(filterParams)).thenReturn(1);
        when(providerService.fetchAllWebUsers()).thenReturn(userMap);

        PageResults<ProviderRow> pageResults = providerPaginationService.page(pageNumber, rowsPerPage, filterParams, new SortParams());

        assertThat(pageResults.getTotalRows(), is(1));
        assertThat(pageResults.getPageNo(), is(pageNumber));
        assertThat(pageResults.getResults().size(), is(1));
        assertThat(pageResults.getResults().get(0).getProviderId(), is(providerId));
        assertThat(pageResults.getResults().get(0).getDistrict(), is(district));


        verify(providerService).count(filterParams);
        verify(providerService).fetchByFilterParams(startIndex, rowsPerPage, district, providerId);
    }

    @Test
    public void shouldReturnResultsModel(){
        Provider provider1 = new ProviderBuilder().withProviderId("p1").build();
        Provider provider2 = new ProviderBuilder().withProviderId("p2").build();
        Provider provider3 = new ProviderBuilder().withProviderId("p3").build();

        MotechUser motechUser = new MotechUser(new MotechWebUser("p2", "password", "externalId", null));
        Map<String, MotechUser> userMap = new HashMap<>();
        userMap.put(motechUser.getUserName(), motechUser);

        when(providerService.fetchAllWebUsers()).thenReturn(userMap);

        List<ProviderRow> providerRowList = providerPaginationService.prepareResultsModel(asList(provider1, provider2, provider3));
        assertThat(providerRowList.size(), is(3));
        assertFalse(providerRowList.get(0).isActive());
        assertTrue(providerRowList.get(1).isActive());
        assertFalse(providerRowList.get(2).isActive());

    }
}