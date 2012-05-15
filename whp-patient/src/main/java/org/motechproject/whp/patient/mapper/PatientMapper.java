package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.DiseaseClass;

public class PatientMapper {

    public static Patient mapBasicInfo(PatientRequest patientRequest) {
        Patient patient = new Patient(
                patientRequest.getCase_id(),
                patientRequest.getFirst_name(),
                patientRequest.getLast_name(),
                patientRequest.getGender(),
                patientRequest.getPatient_type(),
                patientRequest.getMobile_number());
        patient.setPhi(patientRequest.getPhi());
        patient.setLastModifiedDate(patientRequest.getDate_modified());
        return patient;
    }

    public static Treatment mapTreatmentInfo(PatientRequest patientRequest) {
        Treatment treatment = createTreatment(patientRequest);
        mapRegistrationDetails(patientRequest, treatment);
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);
        return treatment;
    }

    static Treatment createTreatment(PatientRequest patientRequest) {
        TreatmentCategory treatmentCategory = patientRequest.getTreatment_category();
        DiseaseClass diseaseClass = patientRequest.getDisease_class();
        int patientAge = patientRequest.getAge();

        return new Treatment(treatmentCategory, diseaseClass, patientAge);
    }

    public static ProvidedTreatment mapProvidedTreatment(PatientRequest patientRequest, Treatment treatment) {
        String providerId = patientRequest.getProvider_id();
        String tbId = patientRequest.getTb_id();
        ProvidedTreatment providedTreatment = new ProvidedTreatment(providerId, tbId);

        providedTreatment.setTreatment(treatment);
        providedTreatment.setStartDate(patientRequest.getDate_modified().toLocalDate()); //Not being set so far?
        mapPatientAddress(patientRequest, providedTreatment);

        return providedTreatment;
    }

    public static ProvidedTreatment createNewProvidedTreatmentForTreatmentCategoryChange(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, Treatment treatment) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        String tbId = treatmentUpdateRequest.getTb_id();

        ProvidedTreatment newProvidedTreatment = new ProvidedTreatment(currentProvidedTreatment.getProviderId(), tbId);

        newProvidedTreatment.setTreatment(treatment);
        newProvidedTreatment.setStartDate(treatmentUpdateRequest.getDate_modified().toLocalDate()); //Not being set so far?
        newProvidedTreatment.setPatientAddress(currentProvidedTreatment.getPatientAddress());

        return newProvidedTreatment;
    }

    public static Patient mapUpdates(PatientRequest patientRequest, Patient patient) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        Treatment currentTreatment = currentProvidedTreatment.getTreatment();

        if (patientRequest.getAge() != null )
            currentTreatment.setPatientAge(patientRequest.getAge());
        if (patientRequest.getMobile_number() != null)
            patient.setPhoneNumber(patientRequest.getMobile_number());

        mapPatientAddress(patientRequest, currentProvidedTreatment);
        mapSmearTestResults(patientRequest, currentTreatment);
        mapWeightStatistics(patientRequest, currentTreatment);

        currentTreatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        patient.setLastModifiedDate(patientRequest.getDate_modified());

        return patient;
    }

    private static void mapPatientAddress(PatientRequest patientRequest, ProvidedTreatment providedTreatment) {
        Address address = patientRequest.getAddress();
        if (!address.isEmpty()) {
            providedTreatment.setPatientAddress(address);
        }
    }

    private static void mapRegistrationDetails(PatientRequest patientRequest, Treatment treatment) {
        treatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        treatment.setStartDate(patientRequest.getTreatmentStartDate());
    }

    private static void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {
        SmearTestResults smearTestResults = patientRequest.getSmearTestResults();
        if (!smearTestResults.isEmpty()) {
            treatment.addSmearTestResult(smearTestResults);
        }
    }

    private static void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        WeightStatistics weightStatistics = patientRequest.getWeightStatistics();
        if (!weightStatistics.isEmpty()) {
            treatment.addWeightStatistics(weightStatistics);
        }
    }

    public static Treatment createNewTreatmentFrom(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest){
        Treatment currentTreatment = patient.getCurrentProvidedTreatment().getTreatment();
        TreatmentCategory treatmentCategory = treatmentUpdateRequest.getTreatment_category();
        DiseaseClass diseaseClass = currentTreatment.getDiseaseClass();
        Integer patientAge = currentTreatment.getPatientAge();

        return new Treatment(treatmentCategory, diseaseClass, patientAge);
    }

}
