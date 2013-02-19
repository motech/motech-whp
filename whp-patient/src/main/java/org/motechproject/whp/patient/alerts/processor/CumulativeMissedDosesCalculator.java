package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CumulativeMissedDosesCalculator {

    private TreatmentWeekInstance treatmentWeekInstance;

    @Autowired
    public CumulativeMissedDosesCalculator(TreatmentWeekInstance treatmentWeekInstance) {
        this.treatmentWeekInstance = treatmentWeekInstance;
    }

    public int getCumulativeMissedDoses(Patient patient) {
        LocalDate tillDate = treatmentWeekInstance.previousAdherenceWeekEndDate();
        return patient.cumulativeMissedDoses(tillDate);
    }
}