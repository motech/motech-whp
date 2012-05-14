package org.motechproject.whp.patient.domain.criteria;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;

public class UpdatePatientCriteria {

    public static boolean canOpenNewTreatment(Patient patient, CriteriaErrors criteriaErrors) {
        if (sanityCheckFails(patient, criteriaErrors)) return false;
        if (!patient.isCurrentTreatmentClosed()) {
            criteriaErrors.add("Current treatment is not closed");
            return false;
        }
        return true;
    }

    public static boolean canCloseCurrentTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, CriteriaErrors criteriaErrors) {
        if (sanityCheckFails(patient, criteriaErrors)) return false;
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        boolean tbIdMatches = true;
        if (!currentProvidedTreatment.getTbId().equals(treatmentUpdateRequest.getTb_id())) {
            criteriaErrors.add("No such tb id for current treatment");
            tbIdMatches = false;
        }
        boolean endDateNotSet = true;
        if (patient.isCurrentTreatmentClosed()) {
            criteriaErrors.add("Current treatment is already closed");
            endDateNotSet = false;
        }
        return tbIdMatches && endDateNotSet;
    }

    public static boolean canPerformSimpleUpdate(Patient patient, PatientRequest patientRequest, CriteriaErrors criteriaErrors) {
        if (sanityCheckFails(patient, criteriaErrors)) return false;
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (!currentProvidedTreatment.getTbId().equals(patientRequest.getTb_id())) {
            criteriaErrors.add("No such tb id for current treatment");
            return false;
        }
        return true;
    }

    public static boolean canTransferInPatient(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, CriteriaErrors criteriaErrors) {
        if (sanityCheckFails(patient, criteriaErrors)) return false;
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (!currentProvidedTreatment.getTbId().equals(treatmentUpdateRequest.getOld_tb_id())) {
            criteriaErrors.add("No such tb id for current treatment");
            return false;
        }
        return true;
    }

    public static boolean canPauseTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, CriteriaErrors criteriaErrors) {
        if (sanityCheckFails(patient, criteriaErrors)) return false;
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (!currentProvidedTreatment.getTbId().equals(treatmentUpdateRequest.getTb_id())) {
            criteriaErrors.add("No such tb id for current treatment");
            return false;
        }
        if (currentProvidedTreatment.isPaused()) {
            criteriaErrors.add("Current treatment is already paused");
            return false;
        }
        return true;
    }

    private static boolean sanityCheckFails(Patient patient, CriteriaErrors criteriaErrors) {
        if (patient == null) {
            criteriaErrors.add("Invalid case-id. No such patient.");
            return true;
        }
        if (!patient.hasCurrentTreatment()) {
            criteriaErrors.add("Case does not have any current treatment");
            return true;
        }
        return false;
    }
}


