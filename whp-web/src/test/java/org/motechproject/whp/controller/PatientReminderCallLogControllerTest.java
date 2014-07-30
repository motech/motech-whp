package org.motechproject.whp.controller;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.http.MediaType.APPLICATION_XML;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.PatientReminderCallLogRequest;

public class PatientReminderCallLogControllerTest {

	PatientReminderController patientReminderController;

    @Mock
    ReportingPublisherService reportingPublisherService;
    @Mock
    PatientService patientService;

    @Before
    public void setUp() {
        initMocks(this);
        patientReminderController = new PatientReminderController(reportingPublisherService,patientService);
    }

    @Test
    public void shouldPublishPatientCallStatusToReporting() throws Exception {
/*
        String requestXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<patient_reminder_call_status>\n" +
                "    <request_id>request_id</request_id>\n" +
                "    <call_id>call_id</call_id>\n" +
                "    <reminder_type>patient_reminder_alert</reminder_type>\n" +
                "    <msisdn>1234567890</msisdn>\n" +
                "    <call_answered>YES</call_answered>\n" +
                "    <disconnection_type>MISSED_CALL</disconnection_type>\n" +
                "    <start_time>null</start_time>\n" +
                "    <end_time>null</end_time> \n" +
                "    <attempt_time>10/12/2012 12:21:45</attempt_time>" +
                "    <attempt>1</attempt>\n" +
                "</patient_reminder_call_status>\n";*/
		String requestXML = "<patient_reminder_call_status><request_id>f9e2aa82-2523-4ddc-9c52-0696b9302948</request_id><call_id>1406354876.128493</call_id><reminder_type>patient_reminder_alert</reminder_type><msisdn>0840701387</msisdn><call_answered>NO</call_answered><disconnection_type>NETWORK_ERROR</disconnection_type><start_time>null</start_time><end_time>null</end_time><attempt_time>26/07/2014 11:37:56</attempt_time><attempt>4</attempt></patient_reminder_call_status>";
        Patient patient = new Patient();
        patient.setPatientId("patientId");
        when(patientService.getPatientByPhoneNumber("1234567890")).thenReturn(patient);

        standaloneSetup(patientReminderController)
                .build()
                .perform(
                        post("/patientReminder/calllog")
                                .body(requestXML.getBytes())
                                .contentType(APPLICATION_XML)
                ).andExpect(content().string(""))
                .andExpect(status().isOk());

        ArgumentCaptor<PatientReminderCallLogRequest> captor = ArgumentCaptor.forClass(PatientReminderCallLogRequest.class);
        verify(reportingPublisherService).reportPatientReminderCallLog(captor.capture());

        PatientReminderCallLogRequest request = captor.getValue();
        assertEquals("request_id", request.getRequestId());
        assertEquals("call_id", request.getCallId());
        assertEquals("patient_reminder_alert", request.getReminderType());
        assertEquals("1234567890", request.getMsisdn());
        assertEquals("YES", request.getCallAnswered());
        assertEquals("MISSED_CALL", request.getDisconnectionType());
        assertEquals("1", request.getAttempt());
        assertEquals("", request.getStartTime());
        assertEquals("", request.getEndTime());
        assertEquals("10/12/2012 12:21:45", request.getAttemptTime());

    }
}
