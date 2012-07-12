package org.motechproject.whp.treatmentcard.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.LocalDate;

@Getter
@EqualsAndHashCode
public class TreatmentPausePeriod {

    private LocalDate startDate;
    private LocalDate endDate;

    public TreatmentPausePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
