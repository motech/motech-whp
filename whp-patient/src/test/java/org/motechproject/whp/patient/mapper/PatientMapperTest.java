package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.patient.domain.Treatment;

import static org.junit.Assert.*;
import static org.motechproject.whp.patient.mapper.PatientMapper.*;

public class PatientMapperTest {

    @Test
    public void shouldMapPatientRequestToPatientDomain() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        Patient patient = mapPatient(patientRequest);
        assertBasicPatientInfo(patient, patientRequest);
        assertProvidedTreatment(patient, patientRequest);
    }

    @Test
    public void shouldMapWeightStatisticsAsEmpty_WhenMissing() {
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withDefaults()
                .withWeightStatistics(null, null, DateUtil.today())
                .build();
        Patient patient = mapPatient(patientRequest);
        ProvidedTreatment providedTreatment = patient.getCurrentProvidedTreatment();
        assertEquals(0, providedTreatment.getWeightInstances().size());
    }

    @Test
    public void mapProvidedTreatmentSetsStartDateOnProvidedTreatment() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId("caseId")
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        Treatment treatment = TreatmentMapper.map(patientRequest);
        ProvidedTreatment providedTreatment = mapProvidedTreatment(patientRequest, treatment);

        assertNotNull(providedTreatment.getStartDate());
    }

    @Test
    public void mapIsMigratedDetail() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientRequest.setMigrated(true);
        Patient patient = PatientMapper.mapBasicInfo(patientRequest);
        assertTrue(patient.isMigrated());

        patientRequest.setMigrated(false);
        patient = PatientMapper.mapBasicInfo(patientRequest);
        assertFalse(patient.isMigrated());
    }


    @Test
    public void newProviderTreatmentForCategoryChange_RetainsOldProviderIdAndAddress_SetsNewTbId_SetsNewTreatment_SetsStartDate() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        Patient patient = mapPatient(patientRequest);

        ProvidedTreatment currentProvidedTreatment = patient.getCurrentProvidedTreatment();

        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .build();

        Treatment newTreatment = TreatmentMapper.createNewTreatment(patient, openNewTreatmentUpdateRequest);

        ProvidedTreatment newProvidedTreatment = createNewProvidedTreatmentForTreatmentCategoryChange(patient, openNewTreatmentUpdateRequest, newTreatment);

        assertNotSame(currentProvidedTreatment.getTbId(), newProvidedTreatment.getTbId());
        assertNotSame(patient.latestTreatment(), newProvidedTreatment.getTreatment());
        assertEquals(openNewTreatmentUpdateRequest.getTb_id(), newProvidedTreatment.getTbId());
        assertEquals(currentProvidedTreatment.getPatientAddress(), newProvidedTreatment.getPatientAddress());
        assertEquals(openNewTreatmentUpdateRequest.getProvider_id(), newProvidedTreatment.getProviderId());
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id(), patient.getPatientId());
        assertEquals(patientRequest.getFirst_name(), patient.getFirstName());
        assertEquals(patientRequest.getLast_name(), patient.getLastName());
        assertEquals(patientRequest.getGender(), patient.getGender());
        assertEquals(patientRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(patientRequest.getPhi(), patient.getPhi());
    }

    private void assertProvidedTreatment(Patient patient, PatientRequest patientRequest) {
        ProvidedTreatment providedTreatment = patient.getCurrentProvidedTreatment();
        assertEquals(patientRequest.getTb_id(), providedTreatment.getTbId());
        assertEquals(patientRequest.getProvider_id(), providedTreatment.getProviderId());
        assertEquals(patientRequest.getTb_registration_number(), providedTreatment.getTbRegistrationNumber());
        assertEquals(patientRequest.getAddress(), providedTreatment.getPatientAddress());

        assertTreatment(patient, patientRequest);
        assertSmearTests(patientRequest, patient.getCurrentProvidedTreatment());
        assertWeightStatistics(patientRequest, patient.getCurrentProvidedTreatment());
    }

    private void assertTreatment(Patient patient, PatientRequest patientRequest) {
        Treatment treatment = patient.latestTreatment();
        assertEquals(patientRequest.getAge(), treatment.getPatientAge());
        assertEquals(patientRequest.getTreatment_category(), treatment.getTreatmentCategory());
        assertNull(treatment.getStartDate());

        assertEquals(patientRequest.getTreatmentStartDate(), treatment.getCreationDate());
    }

    private void assertSmearTests(PatientRequest patientRequest, ProvidedTreatment treatment) {
        SmearTestResults smearTestResults = patientRequest.getSmearTestResults();
        assertEquals(smearTestResults.getSmear_sample_instance(), treatment.getSmearTestInstances().get(0).getSmear_sample_instance());
        assertEquals(smearTestResults.getSmear_test_result_1(), treatment.getSmearTestInstances().get(0).getSmear_test_result_1());
        assertEquals(smearTestResults.getSmear_test_date_1(), treatment.getSmearTestInstances().get(0).getSmear_test_date_1());
        assertEquals(smearTestResults.getSmear_test_result_2(), treatment.getSmearTestInstances().get(0).getSmear_test_result_2());
        assertEquals(smearTestResults.getSmear_test_date_2(), treatment.getSmearTestInstances().get(0).getSmear_test_date_2());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, ProvidedTreatment treatment) {
        assertEquals(patientRequest.getWeightStatistics(), treatment.getWeightInstances().get(0));
    }
}
