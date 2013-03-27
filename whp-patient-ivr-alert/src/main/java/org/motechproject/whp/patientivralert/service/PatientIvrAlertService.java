package org.motechproject.whp.patientivralert.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PatientIvrAlertService {
    public static final int PAGE_SIZE = 10000;
    public static final String OFFSET = "offset";
    public static final String REQUEST_ID = "requestId";
    public static final String PATIENT_ALERTS_CALL_TYPE = "patientAlerts";

    private PatientAdherenceService patientAdherenceService;
    private PatientIVRAlertProperties patientIVRAlertProperties;
    private WGNGateway wgnGateway;
    private UUIDGenerator uuidGenerator;

    @Autowired
    public PatientIvrAlertService(PatientAdherenceService patientAdherenceService,
                                  PatientIVRAlertProperties patientIVRAlertProperties, WGNGateway wgnGateway,
                                  UUIDGenerator uuidGenerator) {
        this.patientAdherenceService = patientAdherenceService;
        this.patientIVRAlertProperties = patientIVRAlertProperties;
        this.wgnGateway = wgnGateway;
        this.uuidGenerator = uuidGenerator;
    }

    @MotechListener(subjects = EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME)
    public void alert(MotechEvent event){
        int offset = (Integer) event.getParameters().get(OFFSET);
        String requestId = (String) event.getParameters().get(REQUEST_ID);

        List<PatientAdherenceRecord> patientAdherenceRecords = patientAdherenceService.getPatientsWithoutAdherence(offset, PAGE_SIZE);

        if(!patientAdherenceRecords.isEmpty()) {
            PatientAlertRequest patientAlertRequest = createPatientAlertsRequest(requestId, patientAdherenceRecords);
            EventCallBack eventCallBack = new EventCallBack(EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME, createNextBatchEventParams(requestId, offset));
            wgnGateway.post(patientIVRAlertProperties.getPatientIVRRequestURL(), patientAlertRequest, eventCallBack);
        }
    }

    private PatientAlertRequest createPatientAlertsRequest(String requestId, List<PatientAdherenceRecord> patientAdherenceRecords) {
        PatientAlertRequest patientAlertRequest = new PatientAlertRequest();
        patientAlertRequest.setBatchId(uuidGenerator.uuid());
        patientAlertRequest.setRequestId(requestId);
        patientAlertRequest.setCallType(PATIENT_ALERTS_CALL_TYPE);
        patientAlertRequest.setData(patientAdherenceRecords);
        return patientAlertRequest;
    }

    private HashMap<String, Object> createNextBatchEventParams(String requestId, int offset) {
        HashMap<String, Object> params = new HashMap<>();
        params.put(OFFSET, offset + PAGE_SIZE);
        params.put(REQUEST_ID, requestId);
        return params;
    }
}
