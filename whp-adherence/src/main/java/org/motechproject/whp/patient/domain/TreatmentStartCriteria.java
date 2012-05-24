package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.refdata.domain.PatientType;

public class TreatmentStartCriteria {

    public static boolean shouldStartOrRestartTreatment(Patient patient, WeeklyAdherence adherence) {
        return isNewTreatmentType(patient) && isAdherenceBeingCapturedForFirstEverWeek(patient, adherence);
    }

    private static boolean isNewTreatmentType(Patient patient) {
        return PatientType.New.compareTo(patient.currentTreatmentType()) == 0;
    }

    private static boolean isAdherenceBeingCapturedForFirstEverWeek(Patient patient, WeeklyAdherence adherence) {
        return isNotOnTreatment(patient.getCurrentProvidedTreatment()) || isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(patient, adherence);
    }

    private static boolean isNotOnTreatment(ProvidedTreatment providedTreatment) {
        return providedTreatment.getTreatment().getStartDate() == null;
    }

    private static boolean isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(Patient patient, WeeklyAdherence adherence) {
        LocalDate currentlySetDoseStartDate = patient.latestTreatment().getStartDate();
        return adherence.getWeek().equals(new TreatmentWeek(currentlySetDoseStartDate));
    }
}
