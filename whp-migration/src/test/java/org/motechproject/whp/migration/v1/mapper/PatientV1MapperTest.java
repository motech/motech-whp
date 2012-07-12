package org.motechproject.whp.migration.v1.mapper;

import org.junit.Test;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.refdata.domain.TreatmentCategory;
import org.motechproject.whp.migration.v0.builder.PatientV0Builder;
import org.motechproject.whp.migration.v0.builder.TherapyV0Builder;
import org.motechproject.whp.migration.v0.builder.TreatmentV0Builder;
import org.motechproject.whp.migration.v0.domain.*;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PatientV1MapperTest {

    @Test
    public void shouldMapBasicPatientInfo() {
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().build();
        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(patientV0.getPatientId(), patient.getPatientId());
        assertEquals(patientV0.getFirstName(), patient.getFirstName());
        assertEquals(patientV0.getLastName(), patient.getLastName());
        assertEquals(patientV0.getGender().toString(), patient.getGender().toString());
        assertEquals(patientV0.getPhoneNumber(), patient.getPhoneNumber());
        assertEquals(patientV0.getPhi(), patient.getPhi());
        assertEquals(patientV0.getStatus().name(), patient.getStatus().name());
        assertEquals(patientV0.getLastModifiedDate(), patient.getLastModifiedDate());
        assertEquals(patientV0.isOnActiveTreatment(), patient.isOnActiveTreatment());
        assertEquals(patientV0.isMigrated(), patient.isMigrated());
        assertEquals("V2", patient.getVersion());
    }

    @Test
    public void shouldMapCurrentTherapy() {
        PatientV0 patientV0 = new PatientV0Builder().withDefaults().build();
        Patient patient = new PatientV1Mapper(patientV0).map();
        assertTherapy(patientV0.getCurrentTreatment().getTherapy(), patient.getCurrentTherapy());
    }

    @Test
    public void shouldMapTherapyHistory_WhenCurrentTherapyHasOneTreatment() {
        TherapyV0 therapyV01 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();
        TherapyV0 therapyV02 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc2").build();
        TherapyV0 therapyV03 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc3").build();

        TreatmentV0 treatmentV01 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV02 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV02).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTherapy(therapyV03).build();
        patientV0.setTreatments(Arrays.asList(treatmentV01, treatmentV02));

        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(2, patient.getTherapyHistory().size());
        assertTherapy(therapyV01, patient.getTherapyHistory().get(0));
        assertTherapy(therapyV02, patient.getTherapyHistory().get(1));
    }

    @Test
    public void shouldMapTherapyHistory_WhenCurrentTherapyHasMultipleTreatments() {
        TherapyV0 therapyV01 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();
        TherapyV0 therapyV02 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc2").build();
        TherapyV0 currentTherapy = new TherapyV0Builder().withDefaults().withTherapyDocId("doc3").build();

        TreatmentV0 treatmentV01 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV02 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV02).build();
        TreatmentV0 treatmentV03 = new TreatmentV0Builder().withDefaults().withTherapy(currentTherapy).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTherapy(currentTherapy).build();
        patientV0.setTreatments(Arrays.asList(treatmentV01, treatmentV02, treatmentV03));

        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(2, patient.getTherapyHistory().size());
        assertTherapy(therapyV01, patient.getTherapyHistory().get(0));
        assertTherapy(therapyV02, patient.getTherapyHistory().get(1));
    }


    @Test
    public void shouldMapTreatment_WhenTherapyHasOneTreatment() {
        TherapyV0 therapyV0 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();
        TreatmentV0 treatmentV0 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV0).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTreatment(treatmentV0).build();

        Patient patient = new PatientV1Mapper(patientV0).map();
        assertNotNull(patient.getCurrentTherapy().getCurrentTreatment());
        assertTreatment(treatmentV0, patient.getCurrentTherapy().getCurrentTreatment());
        assertEquals(0, patient.getCurrentTherapy().getTreatments().size());
    }

    @Test
    public void shouldMapTreatmentHistory_WhenCurrentTherapyHasMultipleTreatments() {
        TherapyV0 therapyV01 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();

        TreatmentV0 treatmentV01 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV02 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV03 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTreatment(treatmentV01).build();
        patientV0.setTreatments(Arrays.asList(treatmentV02, treatmentV03));

        Patient patient = new PatientV1Mapper(patientV0).map();

        assertEquals(2, patient.getCurrentTherapy().getTreatments().size());
        assertTreatment(treatmentV02, patient.getCurrentTherapy().getTreatments().get(0));
        assertTreatment(treatmentV03, patient.getCurrentTherapy().getTreatments().get(1));
    }

    @Test
    public void shouldMapTreatmentsInTherapyHistory_WhenPatientHasMultipleTherapies() {
        TherapyV0 therapyV01 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc1").build();
        TherapyV0 therapyV02 = new TherapyV0Builder().withDefaults().withTherapyDocId("doc2").build();

        TreatmentV0 treatmentV01 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV01).build();
        TreatmentV0 treatmentV02 = new TreatmentV0Builder().withDefaults().withTherapy(therapyV02).build();

        PatientV0 patientV0 = new PatientV0Builder().withDefaults().withCurrentTreatment(treatmentV01).build();
        patientV0.setTreatments(Arrays.asList(treatmentV02));

        Patient patient = new PatientV1Mapper(patientV0).map();
        assertEquals(1, patient.getTherapyHistory().size());
        assertNotNull(patient.getTherapyHistory().get(0).getCurrentTreatment());
        assertTreatment(treatmentV02, patient.getTherapyHistory().get(0).getCurrentTreatment());
    }

    private void assertTreatment(TreatmentV0 treatmentV0, Treatment treatment) {
        assertEquals(treatmentV0.getProviderId(), treatment.getProviderId());
        assertEquals(treatmentV0.getTbId(), treatment.getTbId());
        assertEquals(treatmentV0.getStartDate(), treatment.getStartDate());
        assertEquals(treatmentV0.getEndDate(), treatment.getEndDate());
        assertPatientAddress(treatmentV0.getPatientAddress(), treatment.getPatientAddress());
        assertEquals(treatmentV0.getTreatmentOutcome(), treatment.getTreatmentOutcome());
        assertEquals(treatmentV0.getPatientType().name(), treatment.getPatientType().name());
        assertEquals(treatmentV0.getTbRegistrationNumber(), treatment.getTbRegistrationNumber());
        assertSmearTestResults(treatmentV0.getSmearTestResults(), treatment.getSmearTestResults());
        assertWeightStatistics(treatmentV0.getWeightStatistics(), treatment.getWeightStatistics());
        assertTreatmentInterruptions(treatmentV0.getInterruptions(), treatment.getInterruptions());
    }

    private void assertTreatmentInterruptions(TreatmentInterruptionsV0 interruptionsV0, TreatmentInterruptions interruptions) {
        assertEquals(interruptionsV0.size(), interruptions.size());
        for (int i = 0; i < interruptionsV0.size(); i++) {
            assertTreatmentInterruption(interruptionsV0.get(i), interruptions.get(i));
        }
    }

    private void assertTreatmentInterruption(TreatmentInterruptionV0 interruptionV0, TreatmentInterruption interruption) {
        assertEquals(interruptionV0.getPauseDate(), interruption.getPauseDate());
        assertEquals(interruptionV0.getReasonForPause(), interruption.getReasonForPause());
        assertEquals(interruptionV0.getResumptionDate(), interruption.getResumptionDate());
        assertEquals(interruptionV0.getReasonForResumption(), interruption.getReasonForResumption());
    }

    private void assertWeightStatistics(WeightStatisticsV0 statisticsV0, WeightStatistics statistics) {
        assertEquals(statisticsV0.getAll().size(), statistics.getAll().size());
        for (int i = 0; i < statisticsV0.getAll().size(); i++) {
            assertWeightStatistic(statisticsV0.get(i), statistics.get(i));
        }
    }

    private void assertWeightStatistic(WeightStatisticsRecordV0 recordV0, WeightStatisticsRecord record) {
        assertEquals(recordV0.getWeight_instance().name(), record.getWeight_instance().name());
        assertEquals(recordV0.getMeasuringDate(), record.getMeasuringDate());
        assertEquals(recordV0.getWeight(), record.getWeight());
    }

    private void assertSmearTestResults(SmearTestResultsV0 resultsV0, SmearTestResults results) {
        assertEquals(resultsV0.getAll().size(), results.getAll().size());
        for (int i = 0; i < resultsV0.getAll().size(); i++) {
            assertSmearTestResult(resultsV0.get(i), results.get(i));
        }
    }

    private void assertSmearTestResult(SmearTestRecordV0 recordV0, SmearTestRecord record) {
        assertEquals(recordV0.getSmear_sample_instance().name(), record.getSmear_sample_instance().name());
        assertEquals(recordV0.getSmear_test_date_1(), record.getSmear_test_date_1());
        assertEquals(recordV0.getSmear_test_result_1().name(), record.getSmear_test_result_1().name());
        assertEquals(recordV0.getSmear_test_date_2(), record.getSmear_test_date_2());
        assertEquals(recordV0.getSmear_test_result_2().name(), record.getSmear_test_result_2().name());
    }

    private void assertPatientAddress(AddressV0 addressV0, Address address) {
        assertEquals(addressV0.getAddress_location(), address.getAddress_location());
        assertEquals(addressV0.getAddress_landmark(), address.getAddress_landmark());
        assertEquals(addressV0.getAddress_block(), address.getAddress_block());
        assertEquals(addressV0.getAddress_village(), address.getAddress_village());
        assertEquals(addressV0.getAddress_district(), address.getAddress_district());
        assertEquals(addressV0.getAddress_state(), address.getAddress_state());
    }

    private void assertTherapy(TherapyV0 therapyV0, Therapy therapy) {
        assertEquals(therapyV0.getId(), therapy.getUid());
        assertEquals(therapyV0.getPatientAge(), therapy.getPatientAge());
        assertEquals(therapyV0.getCreationDate(), therapy.getCreationDate());
        assertEquals(therapyV0.getStartDate(), therapy.getStartDate());
        assertEquals(therapyV0.getCloseDate(), therapy.getCloseDate());
        assertEquals(therapyV0.getStatus().name(), therapy.getStatus().name());
        assertEquals(therapyV0.getDiseaseClass().name(), therapy.getDiseaseClass().name());
        assertTreatmentCategory(therapy.getTreatmentCategory(), therapyV0.getTreatmentCategory());
    }

    private void assertTreatmentCategory(TreatmentCategory treatmentCategory, TreatmentCategoryV0 treatmentCategoryV0) {
        assertEquals(treatmentCategoryV0.getName(), treatmentCategory.getName());
        assertEquals(treatmentCategoryV0.getCode(), treatmentCategory.getCode());
        assertEquals(treatmentCategoryV0.getDosesPerWeek(), treatmentCategory.getDosesPerWeek());
        assertEquals(treatmentCategoryV0.getNumberOfDosesInIP(), treatmentCategory.getNumberOfDosesInIP());
        assertEquals(treatmentCategoryV0.getNumberOfDosesInEIP(), treatmentCategory.getNumberOfDosesInEIP());
        assertEquals(treatmentCategoryV0.getNumberOfDosesInCP(), treatmentCategory.getNumberOfDosesInCP());
        assertEquals(treatmentCategoryV0.getNumberOfWeeksOfIP(), treatmentCategory.getNumberOfWeeksOfIP());
        assertEquals(treatmentCategoryV0.getNumberOfWeeksOfEIP(), treatmentCategory.getNumberOfWeeksOfEIP());
        assertEquals(treatmentCategoryV0.getNumberOfWeeksOfCP(), treatmentCategory.getNumberOfWeeksOfCP());
        assertEquals(treatmentCategoryV0.getPillDays(), treatmentCategory.getPillDays());
    }

}
