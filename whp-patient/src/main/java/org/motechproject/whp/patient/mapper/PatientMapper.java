package org.motechproject.whp.patient.mapper;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Address;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Treatment;

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

        ProvidedTreatment newProvidedTreatment = new ProvidedTreatment(treatmentUpdateRequest.getProvider_id(), tbId);

        newProvidedTreatment.setTreatment(treatment);
        newProvidedTreatment.setStartDate(treatmentUpdateRequest.getDate_modified().toLocalDate()); //Not being set so far?
        newProvidedTreatment.setPatientAddress(currentProvidedTreatment.getPatientAddress());

        return newProvidedTreatment;
    }

    public static Patient mapUpdates(PatientRequest patientRequest, Patient patient) {
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        Treatment currentTreatment = currentProvidedTreatment.getTreatment();

        if (patientRequest.getAge() != null)
            currentTreatment.setPatientAge(patientRequest.getAge());
        if (patientRequest.getMobile_number() != null)
            patient.setPhoneNumber(patientRequest.getMobile_number());

        mapPatientAddress(patientRequest, currentProvidedTreatment);
        TreatmentMapper.mapSmearTestResults(patientRequest, currentTreatment);
        TreatmentMapper.mapWeightStatistics(patientRequest, currentTreatment);

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

}
