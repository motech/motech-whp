package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class PatientMapper {

    public Patient map(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        mapSmearTestResults(patientRequest, patient);
        mapTreatment(patientRequest, patient);
        return patient;
    }

    private Patient mapBasicInfo(PatientRequest patientRequest) {
        return new Patient(
                patientRequest.getCase_id(),
                patientRequest.getFirst_name(),
                patientRequest.getLast_name(),
                Gender.get(patientRequest.getGender()),
                PatientType.get(patientRequest.getPatient_type()),
                patientRequest.getPatient_mobile_num());
    }

    private void mapSmearTestResults(PatientRequest patientRequest, Patient patient) {
        LocalDate smearTestDate1 = LocalDate.parse(patientRequest.getSmear_test_date_1());
        LocalDate smearTestDate2 = LocalDate.parse(patientRequest.getSmear_test_date_2());
        patient.setSmearTestResult(new SmearTestResult(
                patientRequest.getSmear_sample_instance_1(), smearTestDate1, patientRequest.getSmear_result_1(),
                patientRequest.getSmear_sample_instance_2(), smearTestDate2, patientRequest.getSmear_result_2()));
    }

    private void mapTreatment(PatientRequest patientRequest, Patient patient) {
        String treatmentCategory = patientRequest.getTreatment_category();
        String providerId = patientRequest.getProvider_id();
        String tbId = patientRequest.getTb_id();
        LocalDate registrationDate = LocalDate.parse(patientRequest.getRegistration_date());

        Treatment treatment = new Treatment(Category.get(treatmentCategory), providerId, tbId);
        treatment.setRegistrationDetails(patientRequest.getRegistration_number(), registrationDate);
        patient.addTreatment(treatment);

        mapAddress(patientRequest, treatment);
    }

    private void mapAddress(PatientRequest patientRequest, Treatment treatment) {
        treatment.setAddress(new Address(
                patientRequest.getPatient_address_house_num(),
                patientRequest.getPatient_address_landmark(),
                patientRequest.getPatient_address_block(),
                patientRequest.getPatient_address_village(),
                patientRequest.getPatient_address_district(),
                patientRequest.getPatient_address_state()));
    }

}
