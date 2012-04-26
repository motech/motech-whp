package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.CreatePatientRequest;
import org.motechproject.whp.patient.domain.*;

public class PatientMapper {

    public Patient map(CreatePatientRequest createPatientRequest) {
        Patient patient = mapBasicInfo(createPatientRequest);

        Treatment treatment = createTreatment(createPatientRequest);
        mapRegistrationDetails(createPatientRequest, treatment);
        mapSmearTestResults(createPatientRequest, treatment);
        mapWeightStatistics(createPatientRequest, treatment);

        mapProvidedTreatment(createPatientRequest, patient, treatment);

        return patient;
    }

    private Patient mapBasicInfo(CreatePatientRequest createPatientRequest) {
        Patient patient = new Patient(
                createPatientRequest.getCaseId(),
                createPatientRequest.getFirstName(),
                createPatientRequest.getLastName(),
                createPatientRequest.getGender(),
                createPatientRequest.getPatientType(),
                createPatientRequest.getMobileNumber());
        patient.setPhi(createPatientRequest.getPhi());
        patient.setLastModifiedDate(createPatientRequest.getLastModifiedDate());
        return patient;
    }

    private void mapPatientAddress(CreatePatientRequest createPatientRequest, ProvidedTreatment providedTreatment) {
        providedTreatment.setPatientAddress(createPatientRequest.getAddress());
    }

    private void mapRegistrationDetails(CreatePatientRequest patientRequest, Treatment treatment) {
        treatment.setTbRegistrationNumber(patientRequest.getTbRegistrationNumber());
        treatment.setStartDate(patientRequest.getTreatmentStartDate());
    }

    private void mapProvidedTreatment(CreatePatientRequest createPatientRequest, Patient patient, Treatment treatment) {
        String providerId = createPatientRequest.getProviderId();
        String tbId = createPatientRequest.getTbId();
        ProvidedTreatment providedTreatment = new ProvidedTreatment(providerId, tbId);

        providedTreatment.setTreatment(treatment);
        providedTreatment.setStartDate(createPatientRequest.getTreatmentStartDate().toLocalDate());
        mapPatientAddress(createPatientRequest, providedTreatment);

        patient.addProvidedTreatment(providedTreatment);
    }

    private Treatment createTreatment(CreatePatientRequest createPatientRequest) {
        TreatmentCategory treatmentCategory = createPatientRequest.getTreatmentCategory();
        DiseaseClass diseaseClass = createPatientRequest.getDiseaseClass();
        int patientAge = createPatientRequest.getAge();

        return new Treatment(treatmentCategory, diseaseClass, patientAge);
    }

    private void mapSmearTestResults(CreatePatientRequest patientRequest, Treatment treatment) {
        treatment.addSmearTestResult(patientRequest.getSmearTestResults());
    }

    private void mapWeightStatistics(CreatePatientRequest patientRequest, Treatment treatment) {
        treatment.addWeightStatistics(patientRequest.getWeightStatistics());
    }


}
