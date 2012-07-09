package org.motechproject.whp.v0.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.motechproject.whp.v0.domain.*;

import java.util.Arrays;
import java.util.List;

public class PatientV0Builder {

    public static final String CASE_ID = "caseid";
    public static final String NEW_TB_ID = "newtbid";
    public static final String NEW_PROVIDER_ID = "newproviderid";
    protected final TreatmentCategory category01 = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
    protected final TreatmentCategory category10 = new TreatmentCategory("RNTCP Category 1", "10", 3, 8, 24, 4, 12, 18, 54, Arrays.asList(DayOfWeek.Monday));


    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    private final PatientV0 patientV0;

    public PatientV0Builder() {
        patientV0 = new PatientV0();
    }

    public PatientV0Builder withDefaults() {
        patientV0.setPatientId(CASE_ID);
        patientV0.setFirstName("firstName");
        patientV0.setLastName("lastName");
        patientV0.setGender(GenderV0.O);
        patientV0.setPhoneNumber("1234567890");
        patientV0.setCurrentTreatment(defaultTreatment());
        return this;
    }

    public PatientV0 build() {
        return patientV0;
    }

    private TreatmentV0 defaultTreatment() {
        LocalDate today = DateUtil.today();
        TreatmentV0 treatmentV0 = new TreatmentV0();
        treatmentV0.setTbId("elevenDigit");
        treatmentV0.setTherapy(defaultTherapy());
        treatmentV0.setPatientAddress(defaultAddress());
        treatmentV0.setPatientType(PatientTypeV0.New);
        treatmentV0.addSmearTestResult(new SmearTestRecordV0(SmearTestSampleInstanceV0.PreTreatment, today, SmearTestResultV0.Negative, today, SmearTestResultV0.Negative));
        treatmentV0.addWeightStatistics(new WeightStatisticsRecordV0(WeightInstanceV0.PreTreatment, 100.0, today));
        treatmentV0.setTbRegistrationNumber("tbRegistrationNumber");
        return treatmentV0;
    }

    private TherapyV0 defaultTherapy() {
        TherapyV0 therapy = new TherapyV0();
        therapy.setTreatmentCategory(new TreatmentCategoryV0("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        therapy.setDiseaseClass(DiseaseClassV0.P);
        return therapy;
    }

    private AddressV0 defaultAddress() {
        return new AddressV0("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public PatientV0Builder withType(PatientTypeV0 type) {
        patientV0.getCurrentTreatment().setPatientType(type);
        return this;
    }

    public PatientV0Builder withPatientId(String patientId) {
        patientV0.setPatientId(patientId);
        return this;
    }

    public PatientV0Builder onTreatmentFrom(LocalDate date) {
        patientV0.latestTherapy().setStartDate(date);
        return this;
    }

    public PatientV0Builder withTbId(String tbId) {
        patientV0.getCurrentTreatment().setTbId(tbId);
        return this;
    }

    public PatientV0Builder withCurrentTreatment(TreatmentV0 currentTreatment) {
        patientV0.setCurrentTreatment(currentTreatment);
        return this;
    }

    public PatientV0Builder withStatus(PatientStatusV0 status) {
        patientV0.setStatus(status);
        return this;
    }

    public PatientV0Builder withMigrated(boolean migrated) {
        patientV0.setMigrated(migrated);
        return this;
    }
}