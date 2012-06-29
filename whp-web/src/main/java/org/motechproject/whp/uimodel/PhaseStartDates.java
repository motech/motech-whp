package org.motechproject.whp.uimodel;

import lombok.Data;
import org.motechproject.whp.common.WHPDate;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Phases;
import org.motechproject.whp.patient.domain.Therapy;

import static org.motechproject.whp.refdata.domain.PhaseName.CP;
import static org.motechproject.whp.refdata.domain.PhaseName.EIP;
import static org.motechproject.whp.refdata.domain.PhaseName.IP;

@Data
public class PhaseStartDates {
    private String patientId;
    private String ipStartDate;
    private String eipStartDate;
    private String cpStartDate;

    public PhaseStartDates() {
    }

    public PhaseStartDates(Patient patient) {
        Therapy therapy = patient.currentTherapy();
        this.patientId = patient.getPatientId();
        this.ipStartDate = WHPDate.date(therapy.getPhase(IP).getStartDate()).value();
        this.eipStartDate = WHPDate.date(therapy.getPhase(EIP).getStartDate()).value();
        this.cpStartDate = WHPDate.date(therapy.getPhase(CP).getStartDate()).value();
    }

    public Patient mapNewPhaseInfoToPatient(Patient patient) {
        Phases phases = patient.currentTherapy().getPhases();
        patient.startTherapy(WHPDate.date(ipStartDate).date());
        phases.setEIPStartDate(WHPDate.date(eipStartDate).date());
        phases.setCPStartDate(WHPDate.date(cpStartDate).date());
        return patient;
    }
}
