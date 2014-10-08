package org.motechproject.whp.patientreminder.ivr;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.motechproject.whp.common.collections.PaginatedList;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patientreminder.model.PatientReminderRequest;
import org.motechproject.whp.reporting.domain.DoNotCallEntrySummary;
import org.motechproject.whp.reporting.service.ReportingDataService;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientReminderAlertService {
	
	private static final String PATIENT_ADHERENCE_REMINDER = "patient_reminder_alert";
	
	private UUIDGenerator uuidGenerator;
    private PatientReminderProperties patientReminderProperties;
    private WGNGateway gateway;
    private ReportingDataService reportingDataService;
    
    
    
    @Autowired
    public PatientReminderAlertService(UUIDGenerator uuidGenerator, PatientReminderProperties patientReminderProperties, WGNGateway gateway, ReportingDataService reportingDataService) {
        this.uuidGenerator = uuidGenerator;
        this.patientReminderProperties = patientReminderProperties;
        this.gateway = gateway;
        this.reportingDataService = reportingDataService;
    }
    
    public void raiseIVRRequest(List<Patient> patients) {
        String uuid = uuidGenerator.uuid();
        for (List<String> somePatients : new PaginatedList<>(getPhoneNumbers(patients), patientReminderProperties.getBatchSize())) {
            PatientReminderRequest patientReminderRequest = new PatientReminderRequest(PATIENT_ADHERENCE_REMINDER, somePatients, uuid);
            gateway.post(patientReminderProperties.getProviderReminderUrl(), patientReminderRequest);
        }
    }

    
    /**
     * @author atish
     * Changed the phone numbers to exclude donotcall phone numbers
     * By calling a function createDoNotCallPhoneNumbersList
     * and get List of donotcall phone numbers
     * and based on that list avoid donotcall phone numbers
     * to be added getPhoneNumbers list.
     * @param patients
     * @return {@link List}
     */ 
    private List<String> getPhoneNumbers(List<Patient> patients) {
 	   Map <String,String> patientsMap = createPatientsMap(patients);
 	   for(Map.Entry<String, String> donotcall : createDoNotCallMap().entrySet()) {
 		        patientsMap.remove(donotcall.getKey());	   			
 	   }
        return new ArrayList<>(patientsMap.values());
    }
    
    
    private Map<String,String> createPatientsMap(List<Patient> patients){
    	Map<String,String> phoneNumbers = new LinkedHashMap<>();
    	for(Patient patient : patients){
    		if(patient.getPhoneNumber() != null && patient.getPhoneNumber() != ""){
    						phoneNumbers.put(createCompositeKey(patient.getPatientId(),patient.getPhoneNumber()), patient.getPhoneNumber());
    				}  
    	}
    	return phoneNumbers;
    }
    /**
     * @author atish
     * Returns List of Donot call phone numbers    
     * @return {@link List}
     */
    private Map<String,String> createDoNotCallMap(){
       	Map<String,String> donotCallPhoneNumbers = new LinkedHashMap<>();
       	List<DoNotCallEntrySummary> doNotCallEntrySummaries = reportingDataService.getDoNotCallPatients();
       	
       	for(DoNotCallEntrySummary doNotCallEntrySummary: doNotCallEntrySummaries){
       		  donotCallPhoneNumbers.put(createCompositeKey(doNotCallEntrySummary.getEntityId(),doNotCallEntrySummary.getMobileNumber()), doNotCallEntrySummary.getMobileNumber());
       	}
       	return donotCallPhoneNumbers;
    }
    
    private String createCompositeKey(String entityId, String patientId) {
    	return entityId+"-"+patientId;
    }
    
  

}
