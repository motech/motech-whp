package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    ProviderService providerService;

    @Autowired
    public PatientMapper(ProviderService providerService) {
        this.providerService = providerService;
    }

    public Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);

        Therapy therapy = new Therapy(patientRequest.getTreatment_category(), patientRequest.getDisease_class(), patientRequest.getAge());
        therapy.setCreationDate(patientRequest.getTreatmentCreationDate());

        Treatment treatment = createTreatment(patientRequest, patientRequest.getAddress());
        patient.addTreatment(treatment, therapy, patientRequest.getTb_registration_date(), patientRequest.getDate_modified());
        return patient;
    }

    public void mapNewTreatmentForCategoryChange(PatientRequest patientRequest, Patient patient) {
        Therapy newTherapy = new Therapy(patientRequest.getTreatment_category(), patientRequest.getDisease_class(), patient.getAge());
        Treatment treatment = createTreatment(patientRequest, patient.getCurrentTreatment().getPatientAddress());
        patient.addTreatment(treatment, newTherapy, patientRequest.getTb_registration_date(), patientRequest.getDate_modified());
    }

    public void mapTreatmentForTransferIn(PatientRequest patientRequest, Patient patient) {
        Treatment newTreatment = createTreatment(patientRequest, patient.getCurrentTreatment().getPatientAddress());

        if (patientRequest.getDisease_class() != null) {
            patient.getCurrentTherapy().setDiseaseClass(patientRequest.getDisease_class());
        }

        patient.addTreatment(newTreatment, patientRequest.getTb_registration_date(), patientRequest.getDate_modified());
    }

    public Patient mapUpdates(PatientRequest patientRequest, Patient patient) {
        Treatment treatment = patient.getTreatmentBy(patientRequest.getTb_id());
        Therapy therapy = patient.getTherapyHaving(patientRequest.getTb_id());

        if (patientRequest.getAge() != null)
            therapy.setPatientAge(patientRequest.getAge());
        if (patientRequest.getMobile_number() != null)
            patient.setPhoneNumber(patientRequest.getMobile_number());
        if (patientRequest.getTb_registration_number() != null)
            patient.getCurrentTreatment().setTbRegistrationNumber(patientRequest.getTb_registration_number());
        if (patientRequest.getTb_registration_date() != null)
            treatment.setStartDate(patientRequest.getTb_registration_date().toLocalDate());

        setPatientAddress(treatment, patientRequest.getAddress());
        updateTestResults(patientRequest, treatment);

        patient.setLastModifiedDate(patientRequest.getDate_modified());

        return patient;
    }

    private Patient mapBasicInfo(PatientRequest patientRequest) {
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


    Treatment createTreatment(PatientRequest patientRequest, Address address) {
        String providerId = patientRequest.getProvider_id();
        Treatment treatment = new Treatment(patientRequest.getProvider_id(), getProviderDistrict(providerId), patientRequest.getTb_id(), patientRequest.getPatient_type());
        treatment.setStartDate(patientRequest.getTb_registration_date().toLocalDate());
        treatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        treatment.setSmearTestResults(patientRequest.getSmearTestResults());
        treatment.setWeightStatistics(patientRequest.getWeightStatistics());

        setPatientAddress(treatment, address);

        return treatment;
    }

    private String getProviderDistrict(String providerId) {
        if(providerId == null){
            return null;
        }
        return providerService.findByProviderId(providerId).getDistrict();
    }

    private void setPatientAddress(Treatment treatment, Address address) {
        if (!address.isEmpty()) {
            treatment.setPatientAddress(address);
        }
    }

    private static void updateTestResults(PatientRequest patientRequest, Treatment treatment) {
        for (SmearTestRecord smearTestRecord : patientRequest.getSmearTestResults().getAll()) {
            treatment.getSmearTestResults().add(smearTestRecord);
        }
        for (WeightStatisticsRecord weightStatisticsRecord : patientRequest.getWeightStatistics().getAll()) {
            treatment.getWeightStatistics().add(weightStatisticsRecord);
        }
    }
}
