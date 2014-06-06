package org.motechproject.whp.patientreminder.ivr;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.List;

import org.motechproject.whp.common.collections.PaginatedList;
import org.motechproject.whp.common.domain.PhoneNumber;
import org.motechproject.whp.common.util.UUIDGenerator;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patientreminder.model.PatientReminderRequest;
import org.motechproject.whp.wgn.outbound.service.WGNGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.jna.FromNativeConverter;
@Component
public class PatientReminderAlertService {
	
	private static final String PATIENT_ADHERENCE_REMINDER = "patient_reminder_alert";
	
	private UUIDGenerator uuidGenerator;
    private PatientReminderProperties patientReminderProperties;
    private WGNGateway gateway;
    
    
    @Autowired
    public PatientReminderAlertService(UUIDGenerator uuidGenerator, PatientReminderProperties patientReminderProperties, WGNGateway gateway) {
        this.uuidGenerator = uuidGenerator;
        this.patientReminderProperties = patientReminderProperties;
        this.gateway = gateway;
    }
    
    public void raiseIVRRequest(List<Patient> patients) {
        String uuid = uuidGenerator.uuid();
        for (List<String> somePatients : new PaginatedList<>(getPhoneNumbers(patients), patientReminderProperties.getBatchSize())) {
            PatientReminderRequest patientReminderRequest = new PatientReminderRequest(PATIENT_ADHERENCE_REMINDER, somePatients, uuid);
            gateway.post(patientReminderProperties.getProviderReminderUrl(), patientReminderRequest);
        }
    }

    
    private List<String> getPhoneNumbers(List<Patient> patients){
    	List<String> phoneNumbers = new ArrayList<>();
    	for(Patient patient : patients){
    		if(patient.getPhoneNumber() != null && patient.getPhoneNumber() != ""){
    			phoneNumbers.add(patient.getPhoneNumber());
    		}
    	}
    	return phoneNumbers;
    }

}
