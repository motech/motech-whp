package org.motechproject.whp.patient.domain;

import org.motechproject.whp.adherence.domain.WeeklyAdherence;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.refdata.domain.PatientType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TreatmentStartCriteria {

    AllPatients allPatients;

    @Autowired
    public TreatmentStartCriteria(AllPatients allPatients) {
        this.allPatients = allPatients;
    }

    public boolean shouldStartTreatment(String patientId, WeeklyAdherence adherence) {
        Patient patient = allPatients.findByPatientId(patientId);
        return isNotOnTreatment(patient.getCurrentProvidedTreatment()) && isNewPatient(patient) && isAnyDoseTaken(adherence);
    }

    private boolean isNotOnTreatment(ProvidedTreatment providedTreatment) {
        return providedTreatment.getTreatment().getDoseStartDate() == null;
    }

    private boolean isNewPatient(Patient patient) {
        return patient.getPatientType().equals(PatientType.New);
    }

    private boolean isAnyDoseTaken(WeeklyAdherence adherence) {
        return adherence.isAnyDoseTaken();
    }

}
