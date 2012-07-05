package org.motechproject.whp.adherence.criteria;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.TreatmentWeek;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;

public class TreatmentStartCriteria {

    public static boolean shouldStartOrRestartTreatment(Patient patient, WeeklyAdherenceSummary adherenceSummary) {
        return isAdherenceBeingCapturedForFirstEverWeek(patient, adherenceSummary);
    }

    private static boolean isAdherenceBeingCapturedForFirstEverWeek(Patient patient, WeeklyAdherenceSummary adherenceSummary) {
        return isNotOnTreatment(patient.getCurrentTreatment()) || isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(patient, adherenceSummary);
    }

    private static boolean isNotOnTreatment(Treatment treatment) {
        return treatment.getTherapy().getStartDate() == null;
    }

    private static boolean isAdherenceBeingRecapturedForTheSameWeekAsTheWeekTreatmentStartedOn(Patient patient, WeeklyAdherenceSummary adherenceSummary) {
        LocalDate currentlySetDoseStartDate = patient.getCurrentTherapy().getStartDate();
        return adherenceSummary.getWeek().equals(new TreatmentWeek(currentlySetDoseStartDate));
    }
}
