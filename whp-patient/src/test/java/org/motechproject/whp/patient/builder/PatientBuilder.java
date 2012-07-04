package org.motechproject.whp.patient.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.joda.time.DateTime.now;

public class PatientBuilder {

    public static final String PATIENT_ID = "patientid";
    public static final String TB_ID = "tbid";
    public static final String PROVIDER_ID = "providerid";


    List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);

    private final Patient patient;

    public PatientBuilder() {
        patient = new Patient();
    }

    public static Patient patient() {
        return new PatientBuilder().withDefaults().build();
    }

    public PatientBuilder withDefaults() {
        patient.setPatientId(PATIENT_ID);
        patient.setFirstName("firstName");
        patient.setLastName("lastName");
        patient.setGender(Gender.O);
        patient.setPhoneNumber("1234567890");
        patient.addTreatment(defaultTreatment(), defaultTherapy(), now());
        return this;
    }

    public PatientBuilder withTherapyUid(String therapyUid) {
        patient.getCurrentTherapy().setUid(therapyUid);
        patient.getCurrentTreatment().setTherapyUid(therapyUid);
        return this;
    }

    public Patient build() {
        return patient;
    }

    private Treatment defaultTreatment() {
        LocalDate today = DateUtil.today();
        Treatment treatment = new Treatment();
        treatment.setTbId(TB_ID);
        treatment.setProviderId(PROVIDER_ID);
        treatment.setTherapy(defaultTherapy());
        treatment.setPatientAddress(defaultAddress());
        treatment.setPatientType(PatientType.New);
        treatment.addSmearTestResult(new SmearTestRecord(SampleInstance.PreTreatment, today, SmearTestResult.Negative, today, SmearTestResult.Negative));
        treatment.addWeightStatistics(new WeightStatisticsRecord(SampleInstance.PreTreatment, 100.0, today));
        treatment.setTbRegistrationNumber("tbRegistrationNumber");
        return treatment;
    }

    private Therapy defaultTherapy() {
        Therapy therapy = new Therapy();
        therapy.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        therapy.setDiseaseClass(DiseaseClass.P);
        therapy.setUid("therapyUid");
        return therapy;
    }

    private Address defaultAddress() {
        return new Address("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public PatientBuilder withType(PatientType type) {
        patient.getCurrentTreatment().setPatientType(type);
        return this;
    }

    public PatientBuilder withPatientId(String patientId) {
        patient.setPatientId(patientId);
        return this;
    }

    public PatientBuilder onTreatmentFrom(LocalDate date) {
        patient.startTherapy(date);
        return this;
    }

    public PatientBuilder withTbId(String tbId) {
        patient.getCurrentTreatment().setTbId(tbId);
        return this;
    }

    public PatientBuilder withCurrentTreatment(Treatment currentTreatment) {
        patient.setCurrentTreatment(currentTreatment);
        return this;
    }

    public PatientBuilder withStatus(PatientStatus status) {
        patient.setStatus(status);
        return this;
    }

    public PatientBuilder withMigrated(boolean migrated) {
        patient.setMigrated(migrated);
        return this;
    }

    public PatientBuilder withCurrentTreatmentStartDate(LocalDate startDate) {
        patient.getCurrentTreatment().setStartDate(startDate);
        return this;
    }

    public PatientBuilder withCurrentTreatmentEndDate(LocalDate endDate) {
        patient.getCurrentTreatment().setEndDate(endDate);
        return this;
    }

    public PatientBuilder withTreatmentUnderProviderId(String providerId) {
        patient.getCurrentTreatment().setProviderId(providerId);
        return this;
    }

    public PatientBuilder withPhi(String phi) {
        patient.setPhi(phi);
        return this;
    }

    public PatientBuilder withGender(Gender gender) {
        patient.setGender(gender);
        return this;
    }

    public PatientBuilder withFirstName(String firstName) {
        patient.setFirstName(firstName);
        return this;
    }

    public PatientBuilder withLastName(String lastName) {
        patient.setLastName(lastName);
        return this;
    }

    public PatientBuilder withPatientMobileNumber(String phoneNumber) {
        patient.setPhoneNumber(phoneNumber);
        return this;
    }

    public PatientBuilder withTreatmentUnderDistrict(String district) {
        patient.getCurrentTreatment().getPatientAddress().setAddress_district(district);
        return this;
    }

    public PatientBuilder withCurrentTherapy(Therapy therapy) {
        patient.setCurrentTherapy(therapy);
        return this;
    }

    public PatientBuilder withCurrentPhaseAsCp(LocalDate cpStartDate) {
        patient.endCurrentPhase(cpStartDate.minusDays(1));
        patient.nextPhaseName(PhaseName.CP);
        patient.startNextPhase();
        return this;
    }

    public PatientBuilder withTherapyStartDate(LocalDate therapyStartDate) {
        patient.getCurrentTherapy().start(therapyStartDate);
        return this;
    }
}
