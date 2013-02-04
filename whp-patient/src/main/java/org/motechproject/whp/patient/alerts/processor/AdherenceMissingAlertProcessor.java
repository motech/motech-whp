package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

@Component
public class AdherenceMissingAlertProcessor implements AlertProcessor {

    TreatmentWeekInstance treatmentWeekInstance;

    @Autowired
    public AdherenceMissingAlertProcessor(TreatmentWeekInstance treatmentWeekInstance) {
        this.treatmentWeekInstance = treatmentWeekInstance;
    }

    @Override
    public int process(Patient patient) {
        if(patient.isCurrentTreatmentPaused())
            return NO_ALERT_VALUE;
        LocalDate tillDate = treatmentWeekInstance.previousAdherenceWeekEndDate();
        return patient.getWeeksElapsedSinceLastDose(tillDate);
    }

    @Override
    public PatientAlertType alertType() {
        return AdherenceMissing;
    }
}
