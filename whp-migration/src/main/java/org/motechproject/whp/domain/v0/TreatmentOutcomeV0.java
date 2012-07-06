package org.motechproject.whp.domain.v0;

public enum TreatmentOutcomeV0 {
    Cured("Cured"), Died("Died"), Failure("Failure"), Defaulted("Defaulted"), TransferredOut("Transferred Out"), SwitchedOverToMDRTBTreatment("Switched Over To MDR-TB Treatment"), TreatmentCompleted("Treatment Completed");

    private String outcome;

    TreatmentOutcomeV0(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome(){
        return outcome;
    }
}