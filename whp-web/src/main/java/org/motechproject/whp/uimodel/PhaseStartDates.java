package org.motechproject.whp.uimodel;

import lombok.Data;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;

import static org.motechproject.whp.common.WHPDate.date;
import static org.motechproject.whp.refdata.domain.PhaseName.*;

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
        this.ipStartDate = date(therapy.getPhase(IP).getStartDate()).value();
        this.eipStartDate = date(therapy.getPhase(EIP).getStartDate()).value();
        this.cpStartDate = date(therapy.getPhase(CP).getStartDate()).value();
    }

    public Patient mapNewPhaseInfoToPatient(Patient patient) {
        patient.adjustPhaseDates(date(ipStartDate).date(), date(eipStartDate).date(), date(cpStartDate).date());
        return patient;
    }
}
