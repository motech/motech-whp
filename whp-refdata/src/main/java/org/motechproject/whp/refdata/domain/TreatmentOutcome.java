package org.motechproject.whp.refdata.domain;

public enum TreatmentOutcome {
    Cured("Cured"), Died("Died"), Failure("Failure"), Defaulted("Defaulted"), TransferredOut("Transferred Out"), SwitchedOverToMDRTBTreatment("Switched Over To MDR-TB Treatment"), TreatmentCompleted("Treatment Completed");

    private String outcome;

    TreatmentOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getOutcome(){
        return outcome;
    }
}
