package org.motechproject.whp.patientivralert.service;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.patientivralert.model.PatientIvrAlertBatchRequest;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientIvrAlertService {
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
        PatientIvrAlertBatchRequest request = new PatientIvrAlertBatchRequest(event);

        List<PatientAdherenceRecord> patientAdherenceRecords = patientAdherenceService.getPatientsWithoutAdherence(
                request.getOffset(),
                patientIVRAlertProperties.getBatchSize());

        if(!patientAdherenceRecords.isEmpty()) {
            PatientAlertRequest patientAlertRequest = createPatientAlertsRequest(patientAdherenceRecords, request);
            EventCallBack eventCallBack = createEventCallBackForNextBatch(request);
            wgnGateway.post(patientIVRAlertProperties.getPatientIVRRequestURL(), patientAlertRequest, eventCallBack);
        }
    }

    private PatientAlertRequest createPatientAlertsRequest(List<PatientAdherenceRecord> patientAdherenceRecords, PatientIvrAlertBatchRequest request) {
        PatientAlertRequest patientAlertRequest = new PatientAlertRequest();
        patientAlertRequest.setBatchId(uuidGenerator.uuid());
        patientAlertRequest.setRequestId(request.getRequestId());
        patientAlertRequest.setCallType(PATIENT_ALERTS_CALL_TYPE);
        patientAlertRequest.setData(patientAdherenceRecords);
        patientAlertRequest.setMessageId(request.getMessageId());
        return patientAlertRequest;
    }

    private EventCallBack createEventCallBackForNextBatch(PatientIvrAlertBatchRequest request) {
        int newOffset = request.getOffset() + patientIVRAlertProperties.getBatchSize();
        PatientIvrAlertBatchRequest nextBatchRequest = new PatientIvrAlertBatchRequest(
                request.getRequestId(),
                request.getMessageId(),
                newOffset);

        return nextBatchRequest.createCallBackEvent();
    }
}
