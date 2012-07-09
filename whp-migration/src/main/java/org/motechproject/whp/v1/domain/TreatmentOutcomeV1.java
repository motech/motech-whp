package org.motechproject.whp.v1.domain;

public enum TreatmentOutcomeV1 {
    Cured("Cured"), Died("Died"), Failure("Failure"), Defaulted("Defaulted"), TransferredOut("Transferred Out"), SwitchedOverToMDRTBTreatment("Switched Over To MDR-TB Treatment"), TreatmentCompleted("Treatment Completed");

    private String outcome;

    TreatmentOutcomeV1(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }
}
