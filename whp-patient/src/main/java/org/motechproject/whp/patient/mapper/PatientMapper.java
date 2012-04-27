package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;

public class PatientMapper {

    public Patient map(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);

        Treatment treatment = createTreatment(patientRequest);
        mapRegistrationDetails(patientRequest, treatment);
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);

        mapProvidedTreatment(patientRequest, patient, treatment);

        return patient;
    }

    private Patient mapBasicInfo(PatientRequest patientRequest) {
        Patient patient = new Patient(
                patientRequest.getCaseId(),
                patientRequest.getFirstName(),
                patientRequest.getLastName(),
                patientRequest.getGender(),
                patientRequest.getPatientType(),
                patientRequest.getMobileNumber());
        patient.setPhi(patientRequest.getPhi());
        patient.setLastModifiedDate(patientRequest.getLastModifiedDate());
        return patient;
    }

    private void mapPatientAddress(PatientRequest patientRequest, ProvidedTreatment providedTreatment) {
        providedTreatment.setPatientAddress(patientRequest.getAddress());
    }

    private void mapRegistrationDetails(PatientRequest patientRequest, Treatment treatment) {
        treatment.setTbRegistrationNumber(patientRequest.getTbRegistrationNumber());
        treatment.setStartDate(patientRequest.getTreatmentStartDate());
    }

    private void mapProvidedTreatment(PatientRequest patientRequest, Patient patient, Treatment treatment) {
        String providerId = patientRequest.getProviderId();
        String tbId = patientRequest.getTbId();
        ProvidedTreatment providedTreatment = new ProvidedTreatment(providerId, tbId);

        providedTreatment.setTreatment(treatment);
        providedTreatment.setStartDate(patientRequest.getTreatmentStartDate().toLocalDate());
        mapPatientAddress(patientRequest, providedTreatment);

        patient.addProvidedTreatment(providedTreatment);
    }

    private Treatment createTreatment(PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatmentCategory();
        DiseaseClass diseaseClass = patientRequest.getDiseaseClass();
        int patientAge = patientRequest.getAge();

        return new Treatment(treatmentCategory, diseaseClass, patientAge);
    }

    private void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {
        treatment.addSmearTestResult(patientRequest.getSmearTestResults());
    }

    private void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        treatment.addWeightStatistics(patientRequest.getWeightStatistics());
    }


}
