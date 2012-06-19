package org.motechproject.whp.patient.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;

import java.util.Arrays;
import java.util.List;

public class TreatmentBuilder {

    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    private final Treatment treatment;

    public TreatmentBuilder() {
        treatment = new Treatment();
    }

    public TreatmentBuilder withDefaults() {
        LocalDate today = DateUtil.today();
        treatment.setTbId("elevenDigit");
        treatment.setTherapy(defaultTherapy());
        treatment.setPatientAddress(defaultAddress());
        treatment.setPatientType(PatientType.New);
        treatment.addSmearTestResult(new SmearTestRecord(SmearTestSampleInstance.PreTreatment, today, SmearTestResult.Negative, today, SmearTestResult.Negative));
        treatment.addWeightStatistics(new WeightStatisticsRecord(WeightInstance.PreTreatment, 100.0, today));
        return this;
    }

    private Therapy defaultTherapy() {
        Therapy therapy = new Therapy();
        therapy.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, 24, 54, threeDaysAWeek));
        therapy.setDiseaseClass(DiseaseClass.P);
        return therapy;
    }

    private Address defaultAddress() {
        return new Address("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public TreatmentBuilder withProviderId(String providerId) {
        treatment.setProviderId(providerId);
        return this;
    }

    public TreatmentBuilder withPatientType(PatientType patientType) {
        treatment.setPatientType(patientType);
        return this;
    }

    public Treatment build() {
        return treatment;
    }

    public TreatmentBuilder withTherapyDocId(String docId) {
        treatment.setTherapyDocId(docId);
        treatment.getTherapy().setId(docId);
        return this;
    }

    public TreatmentBuilder withStartDate(LocalDate date) {
        treatment.setStartDate(date);
        return this;
    }

    public TreatmentBuilder withEndDate(LocalDate date) {
        treatment.setEndDate(date);
        return this;
    }

    public TreatmentBuilder withTbId(String tbId) {
        treatment.setTbId(tbId);
        return this;
    }
}
