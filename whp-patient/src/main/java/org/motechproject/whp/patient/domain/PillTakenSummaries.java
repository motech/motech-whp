package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.TreatmentWeek;

import static org.motechproject.whp.patient.util.WHPDateUtil.isOnOrBefore;

public class PillTakenSummaries {

    @JsonProperty
    private PillTakenSummary olderSummary = new PillTakenSummary();
    @JsonProperty
    private PillTakenSummary latestSummary = new PillTakenSummary();

    public void setPillTakenCount(int dosesTaken, LocalDate asOf) {
        if (latestSummary.isSummaryFor(asOf)) {
            latestSummary.setDoseCount(dosesTaken);
        } else {
            insertNewSummary(dosesTaken, asOf);
        }
    }

    @JsonIgnore
    public int getTotalPillsTaken() {
        return latestSummary.getDoseCount();
    }

    @JsonIgnore
    public int getTotalPillsTakenTillLastSunday(LocalDate reference) {
        if (isOnOrBefore(latestSummary.getSundayDate(), reference)) {
            return latestSummary.getDoseCount();
        } else {
            return olderSummary.getDoseCount();
        }
    }

    private void insertNewSummary(int dosesTaken, LocalDate asOf) {
        olderSummary = new PillTakenSummary(latestSummary);
        latestSummary = new PillTakenSummary(dosesTaken, new TreatmentWeek(asOf).endDate());
    }

    @Data
    private static class PillTakenSummary {

        private int doseCount;
        private LocalDate sundayDate;

        /*Ektorp*/
        private PillTakenSummary() {
        }

        private PillTakenSummary(int doseCount, LocalDate sundayDate) {
            this.doseCount = doseCount;
            this.sundayDate = sundayDate;
        }

        public PillTakenSummary(PillTakenSummary latestSummary) {
            doseCount = latestSummary.getDoseCount();
            sundayDate = latestSummary.getSundayDate();
        }

        @JsonIgnore
        public boolean isAfter(PillTakenSummary summary) {
            return sundayDate.isAfter(summary.getSundayDate());
        }

        @JsonIgnore
        public boolean isSummaryFor(LocalDate reference) {
            return new TreatmentWeek(reference).endDate().equals(sundayDate);
        }
    }
}
