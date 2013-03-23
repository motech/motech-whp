package org.motechproject.whp.reporting.service;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reports.contract.query.PatientAdherenceSummary;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.RequestMatchers.requestTo;
import static org.springframework.test.web.client.ResponseCreators.withSuccess;

public class PatientAdherenceSummaryDataServiceTest {

    ReportingDataService reportingDataService;

    RestTemplate restTemplate;
    @Mock
    ReportingApplicationURLs reportingApplicationURLs;
    private String PATIENT_ADHERENCE_SUMMARY_ENDPOINT = "/patientAdherenceSummary";

    @Before
    public void setUp() {
        initMocks(this);
        restTemplate = new RestTemplate();
        reportingDataService = new ReportingDataService(reportingApplicationURLs, restTemplate);
        when(reportingApplicationURLs.getPatientAdherenceMissingDataURL()).thenReturn(PATIENT_ADHERENCE_SUMMARY_ENDPOINT);
    }

    @Test
    public void shouldGetProviderAdherenceStatusForGivenDistrict() {
        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restTemplate);

        List<PatientAdherenceSummary> adherenceSummaries = asList(new PatientAdherenceSummary("p1", "m1", 1), new PatientAdherenceSummary("p2", "m2", 1));

        mockServer.expect(requestTo(PATIENT_ADHERENCE_SUMMARY_ENDPOINT))
                .andRespond(withSuccess(getJSON(adherenceSummaries).getBytes(), APPLICATION_JSON));

        List<PatientAdherenceSummary> patientsWithMissingAdherence = reportingDataService.getPatientsWithMissingAdherence(0, 1);

        mockServer.verify();

        assertEquals(2, patientsWithMissingAdherence.size());
        assertEquals("p1", patientsWithMissingAdherence.get(0).getPatientId());
        assertEquals("m1", patientsWithMissingAdherence.get(0).getMobileNumber());
        assertEquals(1, patientsWithMissingAdherence.get(0).getMissingWeeks());
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
