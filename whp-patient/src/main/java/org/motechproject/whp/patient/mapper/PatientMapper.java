package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;

public class PatientMapper {

    public static Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);
        Therapy therapy = TherapyMapper.map(patientRequest);
        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, therapy);
        patient.addProvidedTreatment(providedTreatment, patientRequest.getDate_modified());
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

    public static ProvidedTreatment mapProvidedTreatment(PatientRequest patientRequest, Therapy therapy) {
        String providerId = patientRequest.getProvider_id();
        String tbId = patientRequest.getTb_id();
        ProvidedTreatment providedTreatment = new ProvidedTreatment(providerId, tbId, patientRequest.getPatient_type());

        providedTreatment.setTherapy(therapy);
        providedTreatment.setStartDate(patientRequest.getDate_modified().toLocalDate()); //Not being set so far?
        mapSmearTestResults(patientRequest, providedTreatment);
        mapWeightStatistics(patientRequest, providedTreatment);

        providedTreatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        mapPatientAddress(patientRequest, providedTreatment);

        return providedTreatment;
    }

    public static ProvidedTreatment createNewProvidedTreatmentForTreatmentCategoryChange(Patient patient, PatientRequest patientRequest, Therapy therapy) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        String tbId = patientRequest.getTb_id();

        ProvidedTreatment newProvidedTreatment = new ProvidedTreatment(patientRequest.getProvider_id(), tbId, patientRequest.getPatient_type());

        newProvidedTreatment.setTherapy(therapy);
        newProvidedTreatment.setStartDate(patientRequest.getDate_modified().toLocalDate()); //Not being set so far?
        newProvidedTreatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        newProvidedTreatment.setPatientAddress(currentProvidedTreatment.getPatientAddress());
        mapSmearTestResults(patientRequest, newProvidedTreatment);
        mapWeightStatistics(patientRequest, newProvidedTreatment);

        return newProvidedTreatment;
    }

    public static Patient mapUpdates(PatientRequest patientRequest, Patient patient) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        Therapy currentTherapy = patient.latestTreatment();

        if (patientRequest.getAge() != null)
            currentTherapy.setPatientAge(patientRequest.getAge());
        if (patientRequest.getMobile_number() != null)
            patient.setPhoneNumber(patientRequest.getMobile_number());

        mapPatientAddress(patientRequest, currentProvidedTreatment);
        mapSmearTestResults(patientRequest, currentProvidedTreatment);
        mapWeightStatistics(patientRequest, currentProvidedTreatment);

        patient.setLastModifiedDate(patientRequest.getDate_modified());

        return patient;
    }

    private static void mapPatientAddress(PatientRequest patientRequest, ProvidedTreatment providedTreatment) {
        Address address = patientRequest.getAddress();
        if (!address.isEmpty()) {
            providedTreatment.setPatientAddress(address);
        }
    }

    private static void mapSmearTestResults(PatientRequest patientRequest, ProvidedTreatment treatment) {
        for(SmearTestRecord smearTestRecord : patientRequest.getSmearTestResults().getAll()) {
            treatment.getSmearTestResults().add(smearTestRecord);
        }
    }

    private static void mapWeightStatistics(PatientRequest patientRequest, ProvidedTreatment treatment) {
        for(WeightStatisticsRecord weightStatisticsRecord : patientRequest.getWeightStatistics().getAll()) {
            treatment.getWeightStatistics().add(weightStatisticsRecord);
        }
    }
}
