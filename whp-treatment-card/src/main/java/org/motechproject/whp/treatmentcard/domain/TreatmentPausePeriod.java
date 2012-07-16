package org.motechproject.whp.treatmentcard.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.WHPDate;

@Getter
@EqualsAndHashCode
public class TreatmentPausePeriod {

    private String startDate;
    private String endDate;

    public TreatmentPausePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = WHPDate.date(startDate).value();
        this.endDate = WHPDate.date(endDate).value();
    }
}
