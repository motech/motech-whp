package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;

public class PatientMapper {

    public static Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        Therapy therapy = TherapyMapper.map(patientRequest);
        Treatment treatment = mapTreatment(patientRequest, therapy);
        patient.addTreatment(treatment, patientRequest.getDate_modified());
        return patient;
    }

    public static Patient mapBasicInfo(PatientRequest patientRequest) {
        Patient patient = new Patient(
                patientRequest.getCase_id(),
                patientRequest.getFirst_name(),
                patientRequest.getLast_name(),
                patientRequest.getGender(),
                patientRequest.getMobile_number());

        patient.setPhi(patientRequest.getPhi());
        patient.setLastModifiedDate(patientRequest.getDate_modified());
        patient.setMigrated(patientRequest.isMigrated());

        return patient;
    }

    public static Treatment mapTreatment(PatientRequest patientRequest, Therapy therapy) {
        String providerId = patientRequest.getProvider_id();
        String tbId = patientRequest.getTb_id();
        Treatment treatment = new Treatment(providerId, tbId, patientRequest.getPatient_type());

        treatment.setTherapy(therapy);
        treatment.setStartDate(patientRequest.getDate_modified().toLocalDate()); //Not being set so far?
        mapSmearTestResults(patientRequest, treatment);
        mapWeightStatistics(patientRequest, treatment);

        treatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        mapPatientAddress(patientRequest, treatment);

        return treatment;
    }

    public static Treatment createNewTreatmentForTreatmentCategoryChange(Patient patient, PatientRequest patientRequest, Therapy therapy) {
        Treatment currentTreatment = patient.getCurrentTreatment();
        String tbId = patientRequest.getTb_id();

        Treatment newTreatment = new Treatment(patientRequest.getProvider_id(), tbId, patientRequest.getPatient_type());

        newTreatment.setTherapy(therapy);
        newTreatment.setStartDate(patientRequest.getDate_modified().toLocalDate()); //Not being set so far?
        newTreatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        newTreatment.setPatientAddress(currentTreatment.getPatientAddress());
        mapSmearTestResults(patientRequest, newTreatment);
        mapWeightStatistics(patientRequest, newTreatment);

        return newTreatment;
    }

    public static Patient mapUpdates(PatientRequest patientRequest, Patient patient) {
        Treatment currentTreatment = patient.getCurrentTreatment();
        Therapy currentTherapy = patient.latestTreatment();

        if (patientRequest.getAge() != null)
            currentTherapy.setPatientAge(patientRequest.getAge());
        if (patientRequest.getMobile_number() != null)
            patient.setPhoneNumber(patientRequest.getMobile_number());

        mapPatientAddress(patientRequest, currentTreatment);
        mapSmearTestResults(patientRequest, currentTreatment);
        mapWeightStatistics(patientRequest, currentTreatment);

        patient.setLastModifiedDate(patientRequest.getDate_modified());

        return patient;
    }

    private static void mapPatientAddress(PatientRequest patientRequest, Treatment treatment) {
        Address address = patientRequest.getAddress();
        if (!address.isEmpty()) {
            treatment.setPatientAddress(address);
        }
    }

    private static void mapSmearTestResults(PatientRequest patientRequest, Treatment treatment) {
        for(SmearTestRecord smearTestRecord : patientRequest.getSmearTestResults().getAll()) {
            treatment.getSmearTestResults().add(smearTestRecord);
        }
    }

    private static void mapWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        for(WeightStatisticsRecord weightStatisticsRecord : patientRequest.getWeightStatistics().getAll()) {
            treatment.getWeightStatistics().add(weightStatisticsRecord);
        }
    }
}
