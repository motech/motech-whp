package org.motechproject.whp.patientivralert.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.annotations.MotechListener;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.whp.common.event.EventKeys;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.patientivralert.model.PatientAlertRequest;
import org.motechproject.whp.patientivralert.model.PatientIvrAlertBatchRequest;
import org.motechproject.whp.reporting.domain.DoNotCallEntrySummary;
import org.motechproject.whp.reporting.service.ReportingDataService;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatientIvrAlertService {
    public static final String PATIENT_ALERTS_CALL_TYPE = "patientAlerts";

    private PatientAdherenceService patientAdherenceService;
    private PatientIVRAlertProperties patientIVRAlertProperties;
    private WGNGateway wgnGateway;
    private UUIDGenerator uuidGenerator;
    private ReportingDataService reportingDataService;

    @Autowired
    public PatientIvrAlertService(PatientAdherenceService patientAdherenceService,
                                  PatientIVRAlertProperties patientIVRAlertProperties, WGNGateway wgnGateway,
                                  UUIDGenerator uuidGenerator, ReportingDataService reportingDataService) {
        this.patientAdherenceService = patientAdherenceService;
        this.patientIVRAlertProperties = patientIVRAlertProperties;
        this.wgnGateway = wgnGateway;
        this.uuidGenerator = uuidGenerator;
        this.reportingDataService =reportingDataService;
    }

    @MotechListener(subjects = EventKeys.PATIENT_IVR_ALERT_BATCH_EVENT_NAME)
    public void alert(MotechEvent event){
        PatientIvrAlertBatchRequest request = new PatientIvrAlertBatchRequest(event);

        List<PatientAdherenceRecord> patientAdherenceRecords = patientAdherenceService.getPatientsWithoutAdherence(
                request.getOffset(),
                patientIVRAlertProperties.getBatchSize());

        if(!patientAdherenceRecords.isEmpty()) {
            List<PatientAdherenceRecord> patientAdherenceRecordsFiltered = getPatientsAdherenceRecords(patientAdherenceRecords);
            PatientAlertRequest patientAlertRequest = createPatientAlertsRequest(patientAdherenceRecordsFiltered, request);
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
    
    
    /**
     * @author atish
     * Changed the phone numbers to exclude donotcall phone numbers
     * By calling a function createDoNotCallPhoneNumbersList
     * and get List of donotcall phone numbers
     * and based on that list avoid donotcall phone numbers
     * to be added getPatientsAdherenceRecords list.
     * @param patients
     * @return {@link List}
     */ 
    private List<PatientAdherenceRecord> getPatientsAdherenceRecords(List<PatientAdherenceRecord> patientAdherenceRecords) {
 	   Map <String,PatientAdherenceRecord> patientsMap = createPatientsMap(patientAdherenceRecords);
 	   for(Map.Entry<String, String> donotcall : createDoNotCallMap().entrySet()) {
 		        patientsMap.remove(donotcall.getKey());	   			
 	   }
        return new ArrayList<>(patientsMap.values());
    }
    
    
    private Map<String,PatientAdherenceRecord> createPatientsMap(List<PatientAdherenceRecord> patients){
    	Map<String,PatientAdherenceRecord> patientAdherenceRecords = new LinkedHashMap<>();
    	for(PatientAdherenceRecord patient : patients){
    		if(patient.getMobileNumber() != null && patient.getMobileNumber() != ""){
    						patientAdherenceRecords.put(createCompositeKey(patient.getPatientId(),patient.getMobileNumber()), patient);
    				}  
    	}
    	return patientAdherenceRecords;
    }
    /**
     * @author atish
     * Returns List of Donot call phone numbers    
     * @return {@link List}
     */
    private Map<String,String> createDoNotCallMap(){
       	Map<String,String> donotCallPhoneNumbers = new LinkedHashMap<>();
       	List<DoNotCallEntrySummary> doNotCallEntrySummaries = reportingDataService.getDoNotCallPatientsByPatientIvrAlert();
       	
       	for(DoNotCallEntrySummary doNotCallEntrySummary: doNotCallEntrySummaries){
       		  donotCallPhoneNumbers.put(createCompositeKey(doNotCallEntrySummary.getEntityId(),doNotCallEntrySummary.getMobileNumber()), doNotCallEntrySummary.getMobileNumber());
       	}
       	return donotCallPhoneNumbers;
    }
    
    private String createCompositeKey(String entityId, String patientId) {
    	return entityId+"-"+patientId;
    }
    
}
