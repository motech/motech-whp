package org.motechproject.whp.uimodel;

import lombok.Data;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;

import static org.motechproject.whp.common.util.WHPDate.date;

@Data
public class PhaseStartDates {

    private String patientId;
    private String ipStartDate;
    private String eipStartDate;
    private String cpStartDate;

    public PhaseStartDates() {
    }

    public PhaseStartDates(Patient patient) {
        Therapy therapy = patient.getCurrentTherapy();
        this.patientId = patient.getPatientId();
        this.ipStartDate = date(therapy.getPhases().getIPStartDate()).value();
        this.eipStartDate = date(therapy.getPhases().getEIPStartDate()).value();
        this.cpStartDate = date(therapy.getPhases().getCPStartDate()).value();
    }

    public Patient mapNewPhaseInfoToPatient(Patient patient) {
        patient.adjustPhaseDates(date(ipStartDate).date(), date(eipStartDate).date(), date(cpStartDate).date());
        return patient;
    }
}
