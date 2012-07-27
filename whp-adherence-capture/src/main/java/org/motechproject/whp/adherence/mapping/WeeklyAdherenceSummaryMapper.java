package org.motechproject.whp.adherence.mapping;

import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.common.domain.TreatmentWeek;
import org.motechproject.whp.patient.domain.Patient;

import java.util.List;

public class WeeklyAdherenceSummaryMapper {

    private String patientId;
    private TreatmentWeek treatmentWeek;

    public WeeklyAdherenceSummaryMapper(Patient patient, TreatmentWeek treatmentWeek) {
        this.patientId = patient.getPatientId();
        this.treatmentWeek = treatmentWeek;
    }

    public WeeklyAdherenceSummary map(List<Adherence> adherenceList) {
        int dosesTaken = 0;
        for (Adherence adherenceRecord : adherenceList) {
            if (PillStatus.Taken.equals(adherenceRecord.getPillStatus())) {
                dosesTaken++;
            }
        }
        return new WeeklyAdherenceSummary(patientId, treatmentWeek, dosesTaken);
    }

}
