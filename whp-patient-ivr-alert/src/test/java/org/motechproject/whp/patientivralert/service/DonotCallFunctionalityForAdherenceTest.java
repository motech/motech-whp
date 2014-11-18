package org.motechproject.whp.patientivralert.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;

public class DonotCallFunctionalityForAdherenceTest {
	
	private List<PatientAdherenceRecord> inPatients;
	
	private List<String> barredNumbers;
	
	private List<String> phoneNumbers;
	
	private List<PatientAdherenceRecord> records;
	
	@org.junit.Before
	public void init(){
		inPatients = new ArrayList<>();
		barredNumbers = new ArrayList<>();
		inPatients.add(createPatientAdherenceRecords(1,"patient0101", "9618926296"));
		inPatients.add(createPatientAdherenceRecords(2,"patient0102", "9618926297"));
		inPatients.add(createPatientAdherenceRecords(3,"patient0103", "9618927298"));
		inPatients.add(createPatientAdherenceRecords(4,"patient0104", "9618927296"));
		inPatients.add(createPatientAdherenceRecords(5,"patient0101", "9618927299"));
		records = getPatientsAdherenceRecords(inPatients);
		phoneNumbers = new ArrayList<>();
		for(PatientAdherenceRecord record : records) {
			phoneNumbers.add(record.getMobileNumber());
		}
		
	}

	
   
	public PatientAdherenceRecord createPatientAdherenceRecords(int mWeek, String patientId, String mobileNumber) {
		PatientAdherenceRecord p1 = new PatientAdherenceRecord();
		p1.setMissingWeeks(mWeek);
		p1.setPatientId(patientId);
		p1.setMobileNumber(mobileNumber);
	    return p1;	
	}
	
	

    private List<PatientAdherenceRecord> getPatientsAdherenceRecords(List<PatientAdherenceRecord> patientAdherenceRecords) {
 	   Map <String,PatientAdherenceRecord> patientsMap = createPatientsMap(patientAdherenceRecords);
 	   for(Map.Entry<String, String> donotcall : createDoNotCallMap().entrySet()) {
 		  PatientAdherenceRecord removedRecord = patientsMap.remove(donotcall.getKey());
 		  barredNumbers.add(removedRecord.getMobileNumber());
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
  
   
    
    private String createCompositeKey(String entityId, String patientId) {
    	return entityId+"-"+patientId;
    }
    

    private Map<String, String> createDoNotCallMap() {
		   Map<String, String> donotcallPhoneNumbers = new LinkedHashMap<String, String>();
		   donotcallPhoneNumbers.put(createCompositeKey("patient0101","9618926296"), "9618926296");
		   donotcallPhoneNumbers.put(createCompositeKey("patient0102","9618926297"), "9618926297");   
		   return donotcallPhoneNumbers;
	   }
    
       @Test
	   public void getPhoneNumbers() {
		   assertEquals(true, phoneNumbers.get(0).equals("9618927298"));
		   assertEquals(true, phoneNumbers.get(1).equals("9618927296"));
		   assertEquals(true, phoneNumbers.get(2).equals("9618927299"));
		   System.out.println("List of Phone Numbers called : ");
		   for (String phone: phoneNumbers) {
			System.out.println(phone);
		   }
		   
		   assertEquals(true, barredNumbers.get(0).equals("9618926296"));
		   assertEquals(true, barredNumbers.get(1).equals("9618926297"));
		   
		   System.out.println("List of Phone Numbers barred : ");
		   for (String phone: barredNumbers) {
			System.out.println(phone);
		   }
 }

}
