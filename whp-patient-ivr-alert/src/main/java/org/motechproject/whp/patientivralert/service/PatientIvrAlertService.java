package org.motechproject.whp.patientivralert.service;

import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PatientIvrAlertService {
    public static final int PAGE_SIZE = 10000;

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

    public void alert(String requestId, int offset){
        List<PatientAdherenceRecord> patientAdherenceRecords = patientAdherenceService.getPatientsWithoutAdherence(offset, PAGE_SIZE);

        PatientAlertRequest patientAlertRequest = new PatientAlertRequest();
        patientAlertRequest.setBatchId(uuidGenerator.uuid());
        patientAlertRequest.setRequestId(requestId);
        patientAlertRequest.setCallType("patientAlerts");
        patientAlertRequest.setData(patientAdherenceRecords);

        wgnGateway.post(patientIVRAlertProperties.getPatientIVRRequestURL(), patientAlertRequest);
    }
}
