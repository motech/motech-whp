package org.motechproject.whp.patientivralert.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientIvrAlertServiceTest {

    PatientIvrAlertService patientIvrAlertService;

    @Mock
    WGNGateway wgnGateway;
    @Mock
    UUIDGenerator uuidGenerator;
    @Mock
    PatientAdherenceService patientAdherenceService;
    @Mock
    PatientIVRAlertProperties patientIVRAlertProperties;

    private int PAGE_SIZE = 100;

    @Before
    public void setUp() {
        initMocks(this);
        patientIvrAlertService = new PatientIvrAlertService(patientAdherenceService, patientIVRAlertProperties, wgnGateway, uuidGenerator);
    }

    @Test
    public void shouldSendPatientAlertRequestForGivenPageOffset() {
        List<PatientAdherenceRecord> records = asList(new PatientAdherenceRecord("patient1", "mobileNumber", 1));
        String batchId = "batch-id";
        String requestId = "requestId";
        String wgnURL = "wgnURL";
        int offset = 10;
        when(patientAdherenceService.getPatientsWithoutAdherence(offset, PAGE_SIZE)).thenReturn(records);
        when(uuidGenerator.uuid()).thenReturn(batchId);
        when(patientIVRAlertProperties.getPatientIVRRequestURL()).thenReturn(wgnURL);
        when(patientIVRAlertProperties.getBatchSize()).thenReturn(PAGE_SIZE);

        PatientAlertRequest expectedWgnRequest = expectedPatientAlertRequest(records, batchId, requestId);
        EventCallBack expectedEventCallBack = expectedEventCallBack(requestId, offset);

        MotechEvent motechEvent = new MotechEvent(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, createParams(requestId, offset));
        patientIvrAlertService.alert(motechEvent);

        verify(wgnGateway).post(wgnURL, expectedWgnRequest, expectedEventCallBack);
    }

    @Test
    public void shouldNotSendAlertRequestWhenThereAreNoResultsForGivenPageNumber() {
        List<PatientAdherenceRecord> emptyResults = new ArrayList<>();
        String requestId = "requestId";
        int offset = 10;
        when(patientAdherenceService.getPatientsWithoutAdherence(offset, PAGE_SIZE)).thenReturn(emptyResults);

        MotechEvent motechEvent = new MotechEvent(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, createParams(requestId, offset));
        patientIvrAlertService.alert(motechEvent);

        verifyZeroInteractions(wgnGateway);
    }

    private EventCallBack expectedEventCallBack(String requestId, int offset) {
        HashMap<String, Object> params = createParams(requestId, offset + PAGE_SIZE);
        return new EventCallBack(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, params);
    }

    private HashMap<String, Object> createParams(String requestId, int offset) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("requestId", requestId);
        params.put("offset", offset);
        return params;
    }

    private PatientAlertRequest expectedPatientAlertRequest(List<PatientAdherenceRecord> records, String batchId, String requestId) {
        PatientAlertRequest expectedWgnRequest = new PatientAlertRequest();
        expectedWgnRequest.setBatchId(batchId);
        expectedWgnRequest.setRequestId(requestId);
        expectedWgnRequest.setCallType("patientAlerts");
        expectedWgnRequest.setData(records);
        return expectedWgnRequest;
    }
}
