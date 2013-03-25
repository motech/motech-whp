package org.motechproject.whp.patientivralert.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;

import java.util.List;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patientivralert.service.PatientIvrAlertService.PAGE_SIZE;

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
        when(patientAdherenceService.getPatientsWithoutAdherence(10, PAGE_SIZE)).thenReturn(records);
        when(uuidGenerator.uuid()).thenReturn(batchId);
        when(patientIVRAlertProperties.getPatientIVRRequestURL()).thenReturn(wgnURL);

        patientIvrAlertService.alert(requestId, 10);

        PatientAlertRequest expectedWgnRequest = expectedPatientAlertRequest(records, batchId, requestId);
        verify(wgnGateway).post(wgnURL, expectedWgnRequest);
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
