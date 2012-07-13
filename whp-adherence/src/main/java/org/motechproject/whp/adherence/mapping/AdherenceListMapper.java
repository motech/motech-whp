package org.motechproject.whp.adherence.mapping;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.common.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.List;

public class AdherenceListMapper {

    public static AdherenceList map(Patient patient, WeeklyAdherenceSummary weeklyAdherenceSummary) {
        AdherenceList adherenceRecords = new AdherenceList();
        TreatmentCategory treatmentCategory = patient.getCurrentTherapy().getTreatmentCategory();

        List<DayOfWeek> pillDays = treatmentCategory.getPillDays();
        for (DayOfWeek pillDay : pillDays) {
            LocalDate pillDate = weeklyAdherenceSummary.getWeek().dateOf(pillDay);
            Treatment treatment = patient.getTreatment(pillDate);
            PillStatus pillStatus = weeklyAdherenceSummary.pillStatusOn(pillDay, treatmentCategory);
            if (treatment == null) {
                adherenceRecords.add(new Adherence(patient.getPatientId(), patient.currentTherapyId(), pillDay, pillDate, pillStatus, WHPConstants.UNKNOWN, WHPConstants.UNKNOWN));
            } else {
                adherenceRecords.add(new Adherence(patient.getPatientId(), patient.currentTherapyId(), pillDay, pillDate, pillStatus, treatment.getTbId(), treatment.getProviderId()));
            }
        }
        return adherenceRecords;
    }

}
