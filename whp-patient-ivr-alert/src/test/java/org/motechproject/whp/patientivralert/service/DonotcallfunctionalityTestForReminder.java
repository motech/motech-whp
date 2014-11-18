package org.motechproject.whp.patientivralert.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;

public class DonotcallfunctionalityTestForReminder {
	
	private List<Patient> inPatients;
	
	private List<String> phoneNumbers;
	
	private List<String> barredNumbers;
	
	@org.junit.Before
	public void init() {
		inPatients = new ArrayList<>();
		barredNumbers = new ArrayList<>();
		inPatients.add(createTestPatients("patient0101", "9618926296"));
		inPatients.add(createTestPatients("patient0102", "9618926297"));
		inPatients.add(createTestPatients("patient0103", "9618927298"));
		inPatients.add(createTestPatients("patient0104", "9618927296"));
		inPatients.add(createTestPatients("patient0101", "9618927299"));
		phoneNumbers = getPhoneNumbers(inPatients);
	    
		

	}
	 
	 
	 private Patient createTestPatients(String patientId, String phoneNumber){
		 Patient p = new Patient();
		 p.setPatientId(patientId);
		 p.setPhoneNumber(phoneNumber);
		 return p;
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

       private List<String> getPhoneNumbers(List<Patient> patients) {
    	   Map <String,String> patientsMap = createPatientsMap(patients);
    	   for(Map.Entry<String, String> donotcall : createDoNotCallMap().entrySet()) {
    		           barredNumbers.add(patientsMap.remove(donotcall.getKey()));	   			
    	   }
    	   System.out.println("Reduced Map");
    	   for (String s: patientsMap.values()) {
			  
			    System.out.println(s);
		    }
           return new ArrayList<>(patientsMap.values());
       }
	   
	   
	   private Map<String, String> createDoNotCallMap() {
		   Map<String, String> donotcallPhoneNumbers = new LinkedHashMap<String, String>();
		   donotcallPhoneNumbers.put(createCompositeKey("patient0101","9618926296"), "9618926296");
		   donotcallPhoneNumbers.put(createCompositeKey("patient0102","9618926297"), "9618926297");   
		   return donotcallPhoneNumbers;
	   }
	   
	   private String createCompositeKey(String entityId, String patientId) {
	    	return entityId+"-"+patientId;
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
