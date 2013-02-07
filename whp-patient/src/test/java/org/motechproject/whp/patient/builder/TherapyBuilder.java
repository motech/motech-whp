package org.motechproject.whp.patient.builder;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.patient.domain.*;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.model.DayOfWeek.*;
import static org.motechproject.util.DateUtil.today;

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

    public TherapyBuilder withTreatmentCategory(String categoryName, String categoryCode) {
        TreatmentCategory treatmentCategory = new TreatmentCategory(categoryName, categoryCode, 3, 12, 36, 4, 12, 22, 66, asList(Monday, Wednesday, Thursday));
        treatmentCategory.setName(categoryName);
        therapy.setTreatmentCategory(treatmentCategory);
        return this;
    }

    public TherapyBuilder withTreatmentCategory(TreatmentCategory category) {
        therapy.setTreatmentCategory(category);
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

    public TherapyBuilder withCloseDate(LocalDate endDate) {
        therapy.setCloseDate(endDate);
        return this;
    }

    public TherapyBuilder withNoOfDosesTaken(Phase phaseName, int doses) {
        therapy.setNumberOfDosesTaken(phaseName, doses, today());
        return this;
    }

    public TherapyBuilder withTreatment(Treatment treatment) {
        therapy.addTreatment(treatment, DateTime.now());
        return this;
    }

    public TherapyBuilder withTreatmentStartingOn(Treatment treatment, LocalDate startDate) {
        therapy.addTreatment(treatment, DateUtil.newDateTime(startDate));
        return this;
    }

    public TherapyBuilder withDoseInterruptions(DoseInterruptions doseInterruptions) {
        therapy.setDoseInterruptions(doseInterruptions);
        return this;
    }

    public TherapyBuilder withDefaults() {
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        therapy.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        therapy.setDiseaseClass(DiseaseClass.P);

        therapy.getPhases().setIPStartDate(today());
        therapy.endLatestPhase(today());
        therapy.getPhases().setNextPhase(Phase.EIP);
        therapy.getPhases().startNextPhase();
        therapy.endLatestPhase(today());
        therapy.getPhases().setNextPhase(Phase.CP);
        therapy.getPhases().startNextPhase();
        therapy.endLatestPhase(today());
        return this;
    }
}
