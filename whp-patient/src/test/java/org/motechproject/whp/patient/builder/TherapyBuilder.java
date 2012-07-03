package org.motechproject.whp.patient.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import java.util.Arrays;

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
        TreatmentCategory treatmentCategory = new TreatmentCategory(treatmentCategoryName, "01", 3, 12, 36, 4, 12, 22, 66, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Thursday));
        treatmentCategory.setName(treatmentCategoryName);
        therapy.setTreatmentCategory(treatmentCategory);
        return this;
    }

    public TherapyBuilder withDiseaseClass(DiseaseClass diseaseClass) {
        therapy.setDiseaseClass(diseaseClass);
        return this;
    }

    public TherapyBuilder withTherapyUid(String therapyUid) {
        therapy.setUid(therapyUid);
        return this;
    }

    public TherapyBuilder withStartDate(LocalDate startDate) {
        therapy.start(startDate);
        return this;
    }

    public TherapyBuilder withNoOfDoesTaken(PhaseName phaseName, int doses) {
        therapy.getPhase(phaseName).setNumberOfDosesTaken(doses);
        return this;
    }
}
