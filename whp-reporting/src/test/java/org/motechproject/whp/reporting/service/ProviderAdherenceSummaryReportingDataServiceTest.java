package org.motechproject.whp.reporting.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reports.contract.adherence.ProviderAdherenceSummaries;
import org.motechproject.whp.reports.contract.adherence.ProviderAdherenceSummary;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withSuccess;

public class ProviderAdherenceSummaryReportingDataServiceTest {

    public static final String PRIMARY_MOBILE = "primary";
    public static final int ADHERENCE_MISSING_WEEKS = 6;
    public static final boolean ADHERENCE_GIVEN = true;
    public static final String SECONDARY_MOBILE = "secondary";
    public static final String TERTIARY_MOBILE = "tertiary";
    ReportingDataService reportingDataService;

    RestTemplate restTemplate;
    @Mock
    ReportingApplicationURLs reportingApplicationURLs;

    @Before
    public void setUp() {
        initMocks(this);
        restTemplate = new RestTemplate();
        reportingDataService = new ReportingDataService(reportingApplicationURLs, restTemplate);
        when(reportingApplicationURLs.getProviderAdherenceStatusByDistrictURL()).thenReturn("/providerAdherenceSummary/{district}");
    }

    @Test
    public void shouldGetProviderAdherenceStatusForGivenDistrict() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        String district = "district";
        String providerId = "providerId";
        ProviderAdherenceSummary adherenceSummary = createProviderAdherenceSummary(providerId);
        ProviderAdherenceSummaries reportContractAdherenceSummaries = new ProviderAdherenceSummaries(district, asList(adherenceSummary));

        mockServer.expect(requestTo("/providerAdherenceSummary/" + district))
                .andRespond(withSuccess(getJSON(reportContractAdherenceSummaries).getBytes(), APPLICATION_JSON));

        org.motechproject.whp.reporting.domain.ProviderAdherenceSummaries providerAdherenceSummaries = reportingDataService.getProviderAdherenceStatus(district);

        mockServer.verify();

        assertEquals(1, providerAdherenceSummaries.getAdherenceSummaryList().size());
        org.motechproject.whp.reporting.domain.ProviderAdherenceSummary actualAdherenceSummary = providerAdherenceSummaries.getAdherenceSummaryList().get(0);

        assertEquals(providerId, actualAdherenceSummary.getProviderId());
        assertEquals(ADHERENCE_MISSING_WEEKS, actualAdherenceSummary.getAdherenceMissingWeeks());
        assertEquals(ADHERENCE_GIVEN, actualAdherenceSummary.isAdherenceGiven());
        assertEquals(PRIMARY_MOBILE, actualAdherenceSummary.getPrimaryMobile());
        assertEquals(SECONDARY_MOBILE, actualAdherenceSummary.getSecondaryMobile());
        assertEquals(TERTIARY_MOBILE, actualAdherenceSummary.getTertiaryMobile());
    }

    private ProviderAdherenceSummary createProviderAdherenceSummary(String providerId) {
        ProviderAdherenceSummary adherenceSummary = new ProviderAdherenceSummary();
        adherenceSummary.setProviderId(providerId);
        adherenceSummary.setPrimaryMobile(PRIMARY_MOBILE);
        adherenceSummary.setSecondaryMobile(SECONDARY_MOBILE);
        adherenceSummary.setTertiaryMobile(TERTIARY_MOBILE);
        adherenceSummary.setAdherenceMissingWeeks(ADHERENCE_MISSING_WEEKS);
        adherenceSummary.setAdherenceGiven(ADHERENCE_GIVEN);
        return adherenceSummary;
    }

    protected String getJSON(Object object) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writer().writeValueAsString(object);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
