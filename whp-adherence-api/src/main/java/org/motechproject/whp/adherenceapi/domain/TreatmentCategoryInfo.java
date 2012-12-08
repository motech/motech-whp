package org.motechproject.whp.adherenceapi.domain;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.motechproject.whp.adherenceapi.domain.TreatmentProvider.GOVERNMENT;
import static org.motechproject.whp.adherenceapi.domain.TreatmentProvider.PRIVATE;

@EqualsAndHashCode
public class TreatmentCategoryInfo {

    public static final String VALID_RANGE_FROM_FOR_ALL_CATEGORIES = "0";

    private TreatmentCategory treatmentCategory;

    public TreatmentCategoryInfo(TreatmentCategory treatmentCategory) {
        this.treatmentCategory = treatmentCategory;
    }

    public TreatmentProvider getTreatmentProvider() {
        return treatmentCategory.isGovernmentCategory() ? GOVERNMENT : PRIVATE;
    }

    public String getValidRangeFrom() {
        return VALID_RANGE_FROM_FOR_ALL_CATEGORIES;
    }

    public String getValidRangeTo() {
        return treatmentCategory.getDosesPerWeek().toString();
    }
}
