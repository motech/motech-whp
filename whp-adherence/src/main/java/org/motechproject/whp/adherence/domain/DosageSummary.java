package org.motechproject.whp.adherence.domain;

public class DosageSummary {

    String patientId;
    private int totalDoseTakenCount;
    private int totalIdealDoseCount;

    public String getPatientId() {
        return patientId;
    }

    public DosageSummary setPatientId(String patientId) {
        this.patientId = patientId;
        return this;
    }

    public int getTotalDoseTakenCount() {
        return totalDoseTakenCount;
    }

    public DosageSummary setTotalDoseTakenCount(int totalDoseTakenCount) {
        this.totalDoseTakenCount = totalDoseTakenCount;
        return this;
    }

    public int getTotalIdealDoseCount() {
        return totalIdealDoseCount;
    }

    public DosageSummary setTotalIdealDoseCount(int totalIdealDoseCount) {
        this.totalIdealDoseCount = totalIdealDoseCount;
        return this;
    }

}
