package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.joda.time.LocalDate;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

@Data
public class Phase {

    private LocalDate startDate;
    private LocalDate endDate;
    private PhaseName name;
    /*Has to be updated under multiple cases. Identified so far:
    1) CMF Admin adherence update: Triggered by AdherenceController.update() -> PhaseUpdateOrchestrator
    2) Provider adherence update: Triggered by AdherenceController.update() -> PhaseUpdateOrchestrator
    3) CMF Admin startDate/endDate update: Triggered by PatientController.update() -> PhaseUpdateOrchestrator
    4) Phase transition: TODO
    */
    private int numberOfDosesTaken;

    public Phase() {
    }

    public Phase(PhaseName phaseName) {
        this.name = phaseName;
    }

    public boolean hasStarted() {
        return startDate != null;
    }

    public int remainingDoses(TreatmentCategory treatmentCategory) {
        return treatmentCategory.numberOfDosesForPhase(name) - numberOfDosesTaken;
    }

}

