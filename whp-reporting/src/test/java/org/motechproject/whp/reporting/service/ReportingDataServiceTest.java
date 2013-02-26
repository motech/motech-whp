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

public class ReportingDataServiceTest {

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
        ProviderAdherenceSummary adherenceSummary = createProviderAdherenceSummary("providerId");
        ProviderAdherenceSummaries expectedSummaries = new ProviderAdherenceSummaries(district, asList(adherenceSummary));

        mockServer.expect(requestTo("/providerAdherenceSummary/" + district))
                .andRespond(withSuccess(getJSON(expectedSummaries).getBytes(), APPLICATION_JSON));

        ProviderAdherenceSummaries adherenceSummaries = reportingDataService.getProviderAdherenceStatus(district);

        mockServer.verify();
        assertEquals(expectedSummaries, adherenceSummaries);
    }

    private ProviderAdherenceSummary createProviderAdherenceSummary(String providerId) {
        ProviderAdherenceSummary adherenceSummary = new ProviderAdherenceSummary();
        adherenceSummary.setProviderId(providerId);
        adherenceSummary.setPrimaryMobile("primary");
        adherenceSummary.setAdherenceMissingWeeks(6);
        adherenceSummary.setAdherenceGiven(true);
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
