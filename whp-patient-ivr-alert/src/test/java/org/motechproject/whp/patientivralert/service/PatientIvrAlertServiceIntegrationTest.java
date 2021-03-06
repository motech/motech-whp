package org.motechproject.whp.patientivralert.service;

import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.mockito.ArgumentCaptor;
import org.motechproject.event.EventRelay;
import org.motechproject.event.MotechEvent;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.reporting.service.ReportingDataService;
import org.motechproject.whp.reports.contract.query.PatientAdherenceSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.event.EventKeys.PATIENT_IVR_ALERT_EVENT_NAME;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationPatientIVRAlertContext.xml")
public class PatientIvrAlertServiceIntegrationTest extends SpringIntegrationTest {

    @Autowired
    EventRelay eventRelay;

    @ReplaceWithMock
    @Autowired
    ReportingDataService reportingDataService;

    @ReplaceWithMock
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PatientIVRAlertProperties patientIVRAlertProperties;

    @Before
    public void setUp() throws Exception {
        reset(restTemplate);
        reset(reportingDataService);
    }

    @Test
    public void shouldSendRequestsToWGN() throws InterruptedException {
        reset(restTemplate);
        PatientAdherenceSummary patient1 = new PatientAdherenceSummary("patient1", "1234567890", 1);
        PatientAdherenceSummary patient2 = new PatientAdherenceSummary("patient2", "1234567891", 1);
        PatientAdherenceSummary patient3 = new PatientAdherenceSummary("patient3", "1234567892", 2);

        int batchSize = patientIVRAlertProperties.getBatchSize();

        when(reportingDataService.getPatientsWithMissingAdherence(0, batchSize))
                .thenReturn(asList(patient1, patient2));

        when(reportingDataService.getPatientsWithMissingAdherence(batchSize, batchSize))
                .thenReturn(asList(patient3));

        String messageId = "messageId";
        eventRelay.sendEventMessage(createMotechEvent(messageId));

        ArgumentCaptor<HttpEntity> wgnRequestArgumentCaptor1 = ArgumentCaptor.forClass(HttpEntity.class);
        String wgnURL = patientIVRAlertProperties.getPatientIVRRequestURL();

        Thread.sleep(10000);

        verify(restTemplate, times(2)).postForLocation(eq(wgnURL), wgnRequestArgumentCaptor1.capture());

        List<HttpEntity> allValues = wgnRequestArgumentCaptor1.getAllValues();
        PatientAlertRequest patientAlertRequest1 = (PatientAlertRequest) allValues.get(0).getBody();
        assertEquals(messageId, patientAlertRequest1.getMessageId());
        assertEquals(2, patientAlertRequest1.getData().size());
        assertPatientAdherenceSummary(patient1, patientAlertRequest1.getData().get(0));
        assertPatientAdherenceSummary(patient2, patientAlertRequest1.getData().get(1));

        PatientAlertRequest patientAlertRequest2 = (PatientAlertRequest) allValues.get(1).getBody();
        assertEquals(1, patientAlertRequest2.getData().size());
        assertEquals(messageId, patientAlertRequest2.getMessageId());
        assertPatientAdherenceSummary(patient3, patientAlertRequest2.getData().get(0));
    }

    private MotechEvent createMotechEvent(String messageId) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(EventKeys.SCHEDULE_CONFIGURATION_MESSAGE_ID, messageId);
        return new MotechEvent(PATIENT_IVR_ALERT_EVENT_NAME, params);
    }

    @Test
    public void shouldNotSendSubsequentRequestsOnFailure() throws InterruptedException {
        reset(restTemplate);
        PatientAdherenceSummary patient1 = new PatientAdherenceSummary("patient1", "1234567890", 1);
        PatientAdherenceSummary patient2 = new PatientAdherenceSummary("patient2", "1234567891", 1);
        PatientAdherenceSummary patient3 = new PatientAdherenceSummary("patient3", "1234567892", 2);

        when(reportingDataService.getPatientsWithMissingAdherence(0, patientIVRAlertProperties.getBatchSize()))
                .thenReturn(asList(patient1, patient2));

        when(reportingDataService.getPatientsWithMissingAdherence(patientIVRAlertProperties.getBatchSize(), patientIVRAlertProperties.getBatchSize()))
                .thenReturn(asList(patient3));

        when(restTemplate.postForLocation(
                eq(patientIVRAlertProperties.getPatientIVRRequestURL()), any(HttpEntity.class)))
                .thenThrow(new RuntimeException("Testing..."));

        eventRelay.sendEventMessage(createMotechEvent("messageId"));

        ArgumentCaptor<HttpEntity> wgnRequestArgumentCaptor1 = ArgumentCaptor.forClass(HttpEntity.class);
        String wgnURL = patientIVRAlertProperties.getPatientIVRRequestURL();

        Thread.sleep(10000);
        int expectedRetryAttempts = 4;

        verify(restTemplate, times(expectedRetryAttempts)).postForLocation(eq(wgnURL), wgnRequestArgumentCaptor1.capture());

        List<HttpEntity> allValues = wgnRequestArgumentCaptor1.getAllValues();

        PatientAlertRequest patientAlertRequest1 = (PatientAlertRequest) allValues.get(0).getBody();
        PatientAlertRequest patientAlertRequest2 = (PatientAlertRequest) allValues.get(0).getBody();
        PatientAlertRequest patientAlertRequest3 = (PatientAlertRequest) allValues.get(0).getBody();

        String batchId = patientAlertRequest1.getBatchId();
        assertEquals(batchId, patientAlertRequest2.getBatchId());
        assertEquals(batchId, patientAlertRequest3.getBatchId());
    }

    private void assertPatientAdherenceSummary(PatientAdherenceSummary patientAdherenceSummary, PatientAdherenceRecord patientAdherenceRecord) {
        assertEquals(patientAdherenceSummary.getPatientId(), patientAdherenceRecord.getPatientId());
        assertEquals(patientAdherenceSummary.getMobileNumber(), patientAdherenceRecord.getMobileNumber());
        assertEquals(patientAdherenceSummary.getMissingWeeks(), patientAdherenceRecord.getMissingWeeks());
    }
}
