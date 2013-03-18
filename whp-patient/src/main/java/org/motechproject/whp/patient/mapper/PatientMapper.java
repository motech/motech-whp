package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.common.util.WHPDateUtil;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.user.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    ProviderService providerService;
    TreatmentDetailsMapper treatmentDetailsMapper;

    @Autowired
    public PatientMapper(ProviderService providerService, TreatmentDetailsMapper treatmentDetailsMapper) {
        this.providerService = providerService;
        this.treatmentDetailsMapper = treatmentDetailsMapper;
    }

    public Patient mapPatient(PatientRequest patientRequest) {
        Patient patient = mapBasicInfo(patientRequest);

        Therapy therapy = new Therapy(patientRequest.getTreatment_category(), patientRequest.getDisease_class(), patientRequest.getAge());
        therapy.setCreationDate(WHPDateTime.date(patientRequest.getTreatmentCreationDate()).dateTime());

        Treatment treatment = createTreatment(patientRequest, patientRequest.getAddress());
        patient.addTreatment(treatment, therapy,WHPDateUtil.toDateTime(patientRequest.getTb_registration_date()), WHPDateTime.date(patientRequest.getDate_modified()).dateTime());
        return patient;
    }

    public void mapNewTreatmentForCategoryChange(PatientRequest patientRequest, Patient patient) {
        Therapy newTherapy = new Therapy(patientRequest.getTreatment_category(), patientRequest.getDisease_class(), patient.getAge());
        Treatment treatment = createTreatment(patientRequest, patient.getCurrentTreatment().getPatientAddress());
        patient.addTreatment(treatment, newTherapy, WHPDateUtil.toDateTime(patientRequest.getTb_registration_date()), WHPDateTime.date(patientRequest.getDate_modified()).dateTime());
    }

    public void mapTreatmentForTransferIn(PatientRequest patientRequest, Patient patient) {
        Treatment newTreatment = createTreatment(patientRequest, patient.getCurrentTreatment().getPatientAddress());

        if (patientRequest.getDisease_class() != null) {
            patient.getCurrentTherapy().setDiseaseClass(patientRequest.getDisease_class());
        }

        patient.addTreatment(newTreatment, WHPDateUtil.toDateTime(patientRequest.getTb_registration_date()), WHPDateTime.date(patientRequest.getDate_modified()).dateTime());
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
            treatment.setStartDate(WHPDateUtil.toDateTime(patientRequest.getTb_registration_date()).toLocalDate());

        treatmentDetailsMapper.mapWithNullCheck(patientRequest, patient.getCurrentTreatment());

        setPatientAddress(treatment, patientRequest.getAddress());
        updateTestResults(patientRequest, treatment);

        patient.setLastModifiedDate(WHPDateTime.date(patientRequest.getDate_modified()).dateTime());

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
        patient.setLastModifiedDate(WHPDateTime.date(patientRequest.getDate_modified()).dateTime());
        patient.setMigrated(patientRequest.isMigrated());

        if(patientRequest.getDate_of_birth() != null){
            patient.setDateOfBirth(WHPDate.date(patientRequest.getDate_of_birth()).date());
        }

        return patient;
    }

    Treatment createTreatment(PatientRequest patientRequest, Address address) {
        String providerId = patientRequest.getProvider_id();
        Treatment treatment = new Treatment(patientRequest.getProvider_id(), getProviderDistrict(providerId), patientRequest.getTb_id(), patientRequest.getPatient_type());
        treatment.setStartDate(WHPDate.date(patientRequest.getTb_registration_date()).date());
        treatment.setTbRegistrationNumber(patientRequest.getTb_registration_number());
        treatment.setSmearTestResults(patientRequest.getSmearTestResults());
        treatment.setWeightStatistics(patientRequest.getWeightStatistics());
        treatmentDetailsMapper.map(patientRequest, treatment);

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
