package org.motechproject.whp.patient.builder;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.Phase;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.joda.time.DateTime.now;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class PatientBuilder {

    public static final String PATIENT_ID = "patientid";
    public static final String TB_ID = "tbid";
    public static final String PROVIDER_ID = "providerid";
    public static final String THERAPY_UID = "therapyUid";


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
        patient.addTreatment(defaultTreatment(), defaultTherapy(), now(), now());
        patient.setPatientFlag(defaultPatientFlag());
        return this;
    }

    public Patient build() {
        return patient;
    }

    public PatientFlag defaultPatientFlag() {
        PatientFlag patientFlag = new PatientFlag();
        patientFlag.setFlagValue(false);
        return patientFlag;
    }

    public Treatment defaultTreatment() {
        LocalDate today = DateUtil.today();
        Treatment treatment = new Treatment();
        treatment.setTbId(TB_ID);
        treatment.setProviderId(PROVIDER_ID);
        treatment.setPatientAddress(defaultAddress());
        treatment.setPatientType(PatientType.New);
        treatment.addSmearTestResult(new SmearTestRecord(SputumTrackingInstance.PreTreatment, today, SmearTestResult.Negative, today, SmearTestResult.Negative, "labName", "labNumber"));
        treatment.addWeightStatistics(new WeightStatisticsRecord(SputumTrackingInstance.PreTreatment, 100.0, today));
        treatment.setTbRegistrationNumber("tbRegistrationNumber");
        return treatment;
    }

    public Therapy defaultTherapy() {
        Therapy therapy = new Therapy();
        therapy.setTreatmentCategory(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));
        therapy.setDiseaseClass(DiseaseClass.P);
        therapy.setUid(THERAPY_UID);
        return therapy;
    }

    private Address defaultAddress() {
        return new Address("10", "banyan tree", "10", "chambal", "muzzrafapur", "bhiar");
    }

    public PatientBuilder withPrivateCategory() {
        Therapy therapy = new Therapy();
        therapy.setTreatmentCategory(new TreatmentCategory("Private Category 1", "02", 7, 8, 24, 4, 12, 18, 54, new ArrayList<DayOfWeek>()));
        therapy.setDiseaseClass(DiseaseClass.P);
        therapy.setUid(THERAPY_UID);

        patient.setCurrentTherapy(therapy);
        return this;
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

    public PatientBuilder withProviderId(String providerId) {
        patient.getCurrentTreatment().setProviderId(providerId);
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
        patient.endLatestPhase(cpStartDate.minusDays(1));
        patient.nextPhaseName(Phase.CP);
        patient.startNextPhase();
        return this;
    }

    public PatientBuilder withTherapyStartDate(LocalDate therapyStartDate) {
        patient.getCurrentTherapy().start(therapyStartDate);
        return this;
    }

    public PatientBuilder withAdherenceProvidedForLastWeek() {
        patient.setLastAdherenceWeekStartDate(currentAdherenceCaptureWeek().startDate());
        return this;
    }

    public PatientBuilder withAdherenceProvidedForLastWeek(LocalDate date) {
        patient.setLastAdherenceWeekStartDate(date);
        return this;
    }

    public PatientBuilder withAge(int age) {
        patient.getCurrentTherapy().setPatientAge(age);
        return this;
    }

    public PatientBuilder withTherapy(Therapy therapy) {
        patient.setCurrentTherapy(therapy);
        return this;
    }

    public PatientBuilder withCumulativeMissedAlertValue(int cumulativeMissedDoses, int severity, LocalDate alertDate, String severityColor) {
        patient.updatePatientAlert(PatientAlertType.CumulativeMissedDoses, cumulativeMissedDoses, severity, severityColor);
        patient.getPatientAlerts().cumulativeMissedDoseAlert().setAlertDate(alertDate);
        return this;
    }

    public PatientBuilder withAdherenceMissedWeeks(int adherenceMissedWeeks, int severity, LocalDate alertDate, String severityColor) {
        patient.updatePatientAlert(PatientAlertType.AdherenceMissing, adherenceMissedWeeks, severity, severityColor);
        patient.getPatientAlerts().adherenceMissingAlert().setAlertDate(alertDate);
        return this;
    }

    public PatientBuilder withTreatmentNotStartedDays(int treatmentNotStartedDays, int severity, LocalDate alertDate, String severityColor) {
        patient.updatePatientAlert(PatientAlertType.TreatmentNotStarted, treatmentNotStartedDays, severity, severityColor);
        patient.getPatientAlerts().treatmentNotStartedAlert().setAlertDate(alertDate);
        return this;
    }

    public PatientBuilder withProviderDistrict(String district) {
        patient.getCurrentTreatment().setProviderDistrict(district);
        return this;
    }

    public PatientBuilder withDoseInterruptions(DoseInterruptions doseInterruptions) {
        patient.getCurrentTherapy().setDoseInterruptions(doseInterruptions);
        return this;
    }

    public PatientBuilder withTreatmentCategory(TreatmentCategory treatmentCategory) {
        patient.getCurrentTherapy().setTreatmentCategory(treatmentCategory);
        return this;
    }

    public PatientBuilder withPatientFlag(boolean flagValue) {
        patient.getPatientFlag().setFlagValue(flagValue);
        return this;
    }

    public PatientBuilder defaultPatientAlerts() {
        withAdherenceMissedWeeks(2, 1, DateUtil.today(), "yellow");
        withCumulativeMissedAlertValue(2, 1, DateUtil.today(), "blue");
        withTreatmentNotStartedDays(2, 1, DateUtil.today(), "brown");
        return this;
    }
}
