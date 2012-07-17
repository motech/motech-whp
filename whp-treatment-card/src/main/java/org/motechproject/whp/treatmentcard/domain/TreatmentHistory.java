package org.motechproject.whp.treatmentcard.domain;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.util.WHPDate;

@Getter
@EqualsAndHashCode
public class TreatmentHistory {
    private String providerId;
    private String startDate;
    private String endDate;

    public TreatmentHistory(String providerId, LocalDate startDate, LocalDate endDate) {
        this.providerId = providerId;
        this.startDate = WHPDate.date(startDate).value();
        this.endDate = WHPDate.date(endDate).value();
    }
}
