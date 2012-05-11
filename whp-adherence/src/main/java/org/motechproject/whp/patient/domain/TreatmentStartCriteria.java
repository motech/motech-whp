package org.motechproject.whp.patient.domain;

import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class TreatmentStartCriteria {

    public static boolean shouldStartTreatment(Patient patient, WeeklyAdherence adherence) {

        return (isNewPatient(patient)
                && isAnyDoseTaken(adherence))
                && isFirstEverDoseBeingTaken(patient, adherence);
    }

    private static boolean isFirstEverDoseBeingTaken(Patient patient, WeeklyAdherence adherence) {
        return isNotOnTreatment(patient.getCurrentProvidedTreatment()) ||isAdherenceBeingCapturedForEarlierDate(patient, adherence);
    }

    private static boolean isAdherenceBeingCapturedForEarlierDate(Patient patient, WeeklyAdherence adherence) {
        return patient.getCurrentProvidedTreatment().getTreatment().getDoseStartDate().isAfter(adherence.firstDoseTakenOn());
    }

    private static boolean isNotOnTreatment(ProvidedTreatment providedTreatment) {
        return providedTreatment.getTreatment().getDoseStartDate() == null;
    }

    private static boolean isNewPatient(Patient patient) {
        return patient.getPatientType().equals(PatientType.New);
    }

    private static boolean isAnyDoseTaken(WeeklyAdherence adherence) {
        return adherence.isAnyDoseTaken();
    }

}
