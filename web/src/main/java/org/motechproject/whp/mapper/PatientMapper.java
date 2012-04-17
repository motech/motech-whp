package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.request.PatientRequest;

public class PatientMapper {

    public Patient map(PatientRequest patientRequest) {
        Patient patient = new Patient(patientRequest.getCase_id(), patientRequest.getFirst_name(), patientRequest.getLast_name(), Gender.get(patientRequest.getGender()));

        LocalDate smearTestDate1 = LocalDate.parse(patientRequest.getSmear_test_date_1());
        LocalDate smearTestDate2 = LocalDate.parse(patientRequest.getSmear_test_date_2());
        patient.setSmearTestResult(new SmearTestResult(
                patientRequest.getSmear_sample_instance_1(), smearTestDate1, patientRequest.getSmear_result_1(),
                patientRequest.getSmear_sample_instance_2(), smearTestDate2, patientRequest.getSmear_result_2()));

        String treatmentCategory = patientRequest.getTreatment_category();
        String providerId = patientRequest.getProvider_id();
        String tbId = patientRequest.getTb_id();
        LocalDate registrationDate = LocalDate.parse(patientRequest.getRegistration_date());

        Treatment treatment = new Treatment(Category.get(treatmentCategory), providerId, tbId) ;
        treatment.setRegistrationDetails(patientRequest.getRegistration_number(), registrationDate);
        patient.addTreatment(treatment);

        return patient;
    }

}
