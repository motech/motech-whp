package org.motechproject.whp.patient.builder;

import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

public class TherapyBuilder {

    Therapy therapy;

    public TherapyBuilder() {
        therapy = new Therapy();
    }

    public TherapyBuilder withAge(int age) {
        therapy.setPatientAge(age);
        return this;
    }

    public Therapy build() {
        return therapy;
    }

    public TherapyBuilder withTreatmentCategory(String treatmentCategoryName) {
        TreatmentCategory treatmentCategory = new TreatmentCategory();
        treatmentCategory.setName(treatmentCategoryName);
        therapy.setTreatmentCategory(treatmentCategory);
        return this;
    }

    public TherapyBuilder withDiseaseClass(DiseaseClass diseaseClass) {
        therapy.setDiseaseClass(diseaseClass);
        return this;
    }
}
