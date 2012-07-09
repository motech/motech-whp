package org.motechproject.whp.patient.builder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.refdata.domain.DiseaseClass;
import org.motechproject.whp.refdata.domain.Phase;
import org.motechproject.whp.refdata.domain.TreatmentCategory;

import static java.util.Arrays.asList;
import static org.motechproject.model.DayOfWeek.*;

public class TherapyBuilder {

    Therapy therapy;

    public TherapyBuilder() {
        therapy = new Therapy();
        TreatmentCategory treatmentCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, asList(Monday, Wednesday, Friday));
        therapy.setTreatmentCategory(treatmentCategory);
    }

    public TherapyBuilder withAge(int age) {
        therapy.setPatientAge(age);
        return this;
    }

    public Therapy build() {
        return therapy;
    }

    public TherapyBuilder withTreatmentCategory(String treatmentCategoryName) {
        TreatmentCategory treatmentCategory = new TreatmentCategory(treatmentCategoryName, "01", 3, 12, 36, 4, 12, 22, 66, asList(Monday, Wednesday, Thursday));
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

    public TherapyBuilder withNoOfDosesTaken(Phase phaseName, int doses) {
        therapy.setNumberOfDosesTaken(phaseName, doses);
        return this;
    }

    public TherapyBuilder withTreatment(Treatment treatment) {
        therapy.addTreatment(treatment, DateTime.now());
        return this;
    }

}
