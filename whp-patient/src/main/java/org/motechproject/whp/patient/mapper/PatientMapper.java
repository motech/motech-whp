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

    public Patient mapUpdates(PatientRequest patientRequest, Patient patient) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        Treatment currentTreatment = currentProvidedTreatment.getTreatment();

        patient.setPhoneNumber(patientRequest.getMobileNumber());

        mapPatientAddress(patientRequest, currentProvidedTreatment);
        mapSmearTestResults(patientRequest, currentTreatment);
        mapWeightStatistics(patientRequest, currentTreatment);

        currentTreatment.setTbRegistrationNumber(patientRequest.getTbRegistrationNumber());
        patient.setLastModifiedDate(patientRequest.getLastModifiedDate());

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
        Address address = patientRequest.getAddress();
        if (!address.isEmpty()) {
            providedTreatment.setPatientAddress(address);
        }
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
        SmearTestResults smearTestResults = patientRequest.getSmearTestResults();
        if (!smearTestResults.isEmpty()) {
            treatment.addSmearTestResult(smearTestResults);
        }
    }

    private void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        WeightStatistics weightStatistics = patientRequest.getWeightStatistics();
        if (!weightStatistics.isEmpty()) {
            treatment.addWeightStatistics(weightStatistics);
        }
    }

}
