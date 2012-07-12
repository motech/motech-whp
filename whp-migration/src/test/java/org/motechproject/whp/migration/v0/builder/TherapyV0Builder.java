package org.motechproject.whp.migration.v0.builder;

import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.migration.v0.domain.DiseaseClassV0;
import org.motechproject.whp.migration.v0.domain.TherapyStatusV0;
import org.motechproject.whp.migration.v0.domain.TherapyV0;
import org.motechproject.whp.migration.v0.domain.TreatmentCategoryV0;

import java.util.Arrays;
import java.util.List;

public class TherapyV0Builder {

    public static final String THERAPY_DOC_ID = "therapyDocId";

    private String therapyDocId = THERAPY_DOC_ID;

    private TherapyV0 therapyV0;

    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    public TherapyV0Builder() {
        therapyV0 = new TherapyV0();
    }

    public TherapyV0 build() {
        if (therapyDocId != null)
            therapyV0.setId(therapyDocId);
        return therapyV0;
    }

    public TherapyV0Builder withDefaults() {
        therapyV0.setPatientAge(100);
        therapyV0.setCreationDate(DateUtil.now().minusDays(10));
        therapyV0.setStartDate(DateUtil.today().minusDays(9));
        therapyV0.setCloseDate(DateUtil.today());
        therapyV0.setStatus(TherapyStatusV0.Closed);
        therapyV0.setTreatmentCategory(new TreatmentCategoryV0("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        therapyV0.setDiseaseClass(DiseaseClassV0.P);
        return this;
    }

    public TherapyV0Builder withTherapyDocId(String therapyDocId) {
        this.therapyDocId = therapyDocId;
        return this;
    }

}