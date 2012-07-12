package org.motechproject.whp.treatmentcard.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.LocalDate;

@Getter
@EqualsAndHashCode
public class TreatmentHistory {
    private String providerId;
    private LocalDate startDate;
    private LocalDate endDate;

    public TreatmentHistory(String providerId, LocalDate startDate, LocalDate endDate) {
        this.providerId = providerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
