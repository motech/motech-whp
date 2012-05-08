package org.motechproject.whp.patient.domain.criteria;

import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.contract.TreatmentUpdateRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.exception.WHPDomainException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.ValidationErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class UpdatePatientCriteria {

    public static boolean canOpenNewTreatment(Patient patient, CriteriaErrors criteriaErrors) {
        if (patient == null) {
            criteriaErrors.add("Invalid case-id. No such patient.");
            return false;
        }
        if (!patient.hasCurrentTreatment()) {
            criteriaErrors.add("Case does not have any current treatment");
            return false;
        }
        if (!patient.isCurrentTreatmentClosed()) {
            criteriaErrors.add("Current treatment is not closed");
            return false;
        }
        return true;
    }

    public static boolean canCloseCurrentTreatment(Patient patient, TreatmentUpdateRequest treatmentUpdateRequest, CriteriaErrors criteriaErrors) {
        if (patient == null) {
            criteriaErrors.add("Invalid case-id. No such patient.");
            return false;
        }
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (!patient.hasCurrentTreatment()) {
            criteriaErrors.add("Case does not have any current treatment");
            return false;
        }
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

    public static boolean canPerformSimpleUpdate(Patient patient, PatientRequest patientRequest, CriteriaErrors criteriaErrors){
        if (patient == null) {
            criteriaErrors.add("Invalid case-id. No such patient.");
            return false;
        }
        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();
        if (!patient.hasCurrentTreatment()) {
            criteriaErrors.add("Case does not have any current treatment");
            return false;
        }
        if (!currentProvidedTreatment.getTbId().equals(patientRequest.getTb_id())) {
            criteriaErrors.add("No such tb id for current treatment");
            return false;
        }
        return true;
    }
}

