package org.motechproject.whp.patient.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;

@Data
@NoArgsConstructor
public class PatientAdherenceStatus {
    private String patientId;
    private LocalDate lastAdherenceReportedDate;

    public PatientAdherenceStatus(String patientId, LocalDate lastAdherenceReportedDate) {
        this.patientId = patientId;
        this.lastAdherenceReportedDate = lastAdherenceReportedDate;
    }

    public boolean hasAdherenceForLastReportingWeekForCurrentTherapy() {
        LocalDate lastAdherenceReportWeek = TreatmentWeekInstance.currentAdherenceCaptureWeek().startDate();
        if (lastAdherenceReportedDate == null || lastAdherenceReportedDate.isBefore(lastAdherenceReportWeek))
            return false;
        return true;
    }
}
