package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.domain.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherence;

public class TreatmentStartCriteria {

    public static boolean shouldStartOrRestartTreatment(Patient patient, WeeklyAdherence adherence) {
        return isAdherenceBeingCapturedForFirstEverWeek(patient, adherence) && !patient.isMigrated();
    }

    private static boolean isAdherenceBeingCapturedForFirstEverWeek(Patient patient, WeeklyAdherence adherence) {
        return isNotOnTreatment(patient.getCurrentTreatment()) || isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(patient, adherence);
    }

    private static boolean isNotOnTreatment(Treatment treatment) {
        return treatment.getTherapy().getStartDate() == null;
    }

    private static boolean isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(Patient patient, WeeklyAdherence adherence) {
        LocalDate currentlySetDoseStartDate = patient.latestTherapy().getStartDate();
        return adherence.getWeek().equals(new TreatmentWeek(currentlySetDoseStartDate));
    }
}
