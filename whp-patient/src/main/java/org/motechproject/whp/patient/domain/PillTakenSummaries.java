package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.collections.BoundPriorityQueue;
import org.motechproject.whp.common.domain.TreatmentWeek;

import java.io.Serializable;

import static org.motechproject.whp.common.util.WHPDateUtil.isOnOrBefore;

public class PillTakenSummaries implements Serializable {

    public static final int SIZE = 2;
    @JsonProperty
    private BoundPriorityQueue<PillTakenSummary> summaries = new BoundPriorityQueue<>(SIZE);

    public void setPillTakenCount(int dosesTaken, LocalDate asOf) {
        PillTakenSummary pillTakenSummary = new PillTakenSummary(dosesTaken, new TreatmentWeek(asOf).endDate());
        summaries.insert(pillTakenSummary);
    }

    @JsonIgnore
    public int getTotalPillsTaken() {
        return dosesInLaterSummary();
    }

    @JsonIgnore
    public int getTotalPillsTakenTillLastSunday(LocalDate reference) {
        if (laterSummary() != null && isOnOrBefore(laterSummary().getSundayDate(), reference)) {
            return laterSummary().getDoseCount();
        } else {
            return dosesInOlderSummary();
        }
    }

    private int dosesInOlderSummary() {
        if (summaries.size() < SIZE) {
            return 0;
        } else {
            return summaries.get(0).getDoseCount();
        }
    }

    private int dosesInLaterSummary() {
        if (summaries.size() == 0) {
            return 0;
        } else {
            return laterSummary().getDoseCount();
        }
    }

    private PillTakenSummary laterSummary() {
        return summaries.peek();
    }

    @Data
    private static class PillTakenSummary implements Comparable<PillTakenSummary>, Serializable {

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

        @Override
        public int compareTo(PillTakenSummary other) {
            if (sundayDate == null) {
                if (other.sundayDate == null) {
                    return 0;
                } else {
                    return -1;
                }
            } else if (other.sundayDate == null) {
                return 1;
            } else {
                return sundayDate.compareTo(other.sundayDate);
            }
        }
    }
}
