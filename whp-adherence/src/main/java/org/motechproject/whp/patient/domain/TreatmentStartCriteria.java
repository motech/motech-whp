package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.refdata.domain.PatientType;

public class TreatmentStartCriteria {

    public static boolean shouldStartOrRestartTreatment(Patient patient, WeeklyAdherence adherence) {
        return isNewPatient(patient) && isAdherenceBeingCapturedForFirstEverWeek(patient, adherence);
    }

    private static boolean isNewPatient(Patient patient) {
        return patient.getPatientType().equals(PatientType.New);
    }

    private static boolean isAdherenceBeingCapturedForFirstEverWeek(Patient patient, WeeklyAdherence adherence) {
        return isNotOnTreatment(patient.getCurrentProvidedTreatment()) || isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(patient, adherence);
    }

    private static boolean isNotOnTreatment(ProvidedTreatment providedTreatment) {
        return providedTreatment.getTreatment().getDoseStartDate() == null;
    }

    private static boolean isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(Patient patient, WeeklyAdherence adherence) {
        LocalDate currentlySetDoseStartDate = patient.getCurrentProvidedTreatment().getTreatment().getDoseStartDate();
        return adherence.getWeek().equals(new TreatmentWeek(currentlySetDoseStartDate));
    }
}
