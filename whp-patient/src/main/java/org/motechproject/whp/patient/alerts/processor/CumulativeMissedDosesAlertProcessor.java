package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;

@Component
public class CumulativeMissedDosesAlertProcessor implements AlertProcessor {

    private TreatmentWeekInstance treatmentWeekInstance;

    @Autowired
    public CumulativeMissedDosesAlertProcessor(TreatmentWeekInstance treatmentWeekInstance) {
        this.treatmentWeekInstance = treatmentWeekInstance;
    }

    @Override
    public int process(Patient patient) {
        if(patient.isCurrentTreatmentPaused()){
            return NO_ALERT_VALUE;
        }

        LocalDate tillDate = treatmentWeekInstance.previousAdherenceWeekEndDate();
        return patient.cumulativeMissedDoses(tillDate);
    }

    @Override
    public PatientAlertType alertType() {
        return CumulativeMissedDoses;
    }
}
