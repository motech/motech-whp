package org.motechproject.whp.v1.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.WHPDate;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

@Data
public class PhaseV1 {

    private LocalDate startDate;
    private LocalDate endDate;
    private PhaseNameV1 name;
    /*Has to be updated under multiple cases. Identified so far:
    1) CMF Admin adherence update: Triggered by AdherenceController.update() -> PhaseUpdateOrchestrator
    2) Provider adherence update: Triggered by AdherenceController.update() -> PhaseUpdateOrchestrator
    3) CMF Admin startDate/endDate update: Triggered by PatientController.update() -> PhaseUpdateOrchestrator
    4) Phase transition: TODO
    */
    private int numberOfDosesTaken;

    public PhaseV1() {
    }

    public PhaseV1(PhaseNameV1 phaseName) {
        this.name = phaseName;
    }

    public boolean hasStarted() {
        return startDate != null;
    }

    public int remainingDoses(TreatmentCategoryV1 treatmentCategory) {
        return treatmentCategory.numberOfDosesForPhase(name) - numberOfDosesTaken;
    }

    public void start(PhaseV1 previousPhase) {
        this.startDate = previousPhase.endDate.plusDays(1);
    }

    public void stop(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void unset() {
        this.startDate = null;
        this.endDate = null;
    }

    @JsonIgnore
    public String getEndDateAsString() {
        return WHPDate.date(endDate).value();
    }
}

