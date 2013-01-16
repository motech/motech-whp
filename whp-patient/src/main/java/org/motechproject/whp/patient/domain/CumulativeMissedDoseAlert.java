package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;

@Data
public class CumulativeMissedDoseAlert extends PatientAlert {
    private LocalDate resetDate;

    public CumulativeMissedDoseAlert() {
    }

    public CumulativeMissedDoseAlert(int threshold, int value, LocalDate alertDate) {
        super(threshold, value, alertDate);
    }

    public LocalDate getDateOfReferenceForCumulativeMissedDoses(LocalDate treatmentStartDate){
        return resetDate != null && resetDate.isAfter(treatmentStartDate) ? resetDate : treatmentStartDate;
    }
}
