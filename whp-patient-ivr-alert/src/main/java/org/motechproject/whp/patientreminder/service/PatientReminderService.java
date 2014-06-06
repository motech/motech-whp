package org.motechproject.whp.patientreminder.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patientreminder.ivr.PatientReminderAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class PatientReminderService {

	private AllPatients allPatients;
	private static final String GOVERNMENT_CATEGORY_VERSION1 = "RNTCP Category 1" ;
	private static final String GOVERNMENT_CATEGORY_VERSION2 = "RNTCP Category 2" ;
	private static final String PRIVATE_CATEGORY_VERSION1 = "Commercial/Private Category 1" ;
	private static final String PRIVATE_CATEGORY_VERSION2 = "Commercial/Private Category 2" ;
	
	private PatientReminderAlertService patientAlertService;
	@Autowired
	public PatientReminderService(AllPatients allPatients , PatientReminderAlertService patientAlertService){
		this.allPatients = allPatients;
		this.patientAlertService = patientAlertService;
	}
	
	private List<Patient> getPatientsUnderGovernmentTreatmentCategory(){
		List<Patient> patients = allPatients.findByTreatmentType(GOVERNMENT_CATEGORY_VERSION1);
		patients.addAll(allPatients.findByTreatmentType(GOVERNMENT_CATEGORY_VERSION2));
		return patients;
	}
	
	private List<Patient> getPatientsUnderPrivateTreatmentCategory(){
		List<Patient> patients = allPatients.findByTreatmentType(PRIVATE_CATEGORY_VERSION1);
		patients.addAll(allPatients.findByTreatmentType(PRIVATE_CATEGORY_VERSION2));
		return patients;
	}
	
	public void alertPatientsUnderGovernmentTreatmentCategory(){
		List<Patient> patients = getPatientsUnderGovernmentTreatmentCategory();
        if (CollectionUtils.isNotEmpty(patients)) {
        	patientAlertService.raiseIVRRequest(patients);
        }
	}
	
	public void alertPatientsUnderCommercialTreatmentCategory(){
		List<Patient> patients = getPatientsUnderPrivateTreatmentCategory();
        if (CollectionUtils.isNotEmpty(patients)) {
        	patientAlertService.raiseIVRRequest(patients);
        }
	}
}
