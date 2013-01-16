package org.motechproject.whp.patient.domain;

import org.joda.time.LocalDate;

public class CumulativeMissedDoseAlert extends PatientAlert {

    public CumulativeMissedDoseAlert() {
        super(PatientAlertType.CumulativeMissedDoses);
    }

    public LocalDate getDateOfReferenceForCumulativeMissedDoses(LocalDate treatmentStartDate){
        return getResetDate() != null && getResetDate().isAfter(treatmentStartDate) ? getResetDate() : treatmentStartDate;
    }
}
