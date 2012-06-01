package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.SmearTestRecord;
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
                .withMandatoryFieldsForImportPatient()
                .withWeightStatistics(null, null, DateUtil.today())
                .build();
        Patient patient = mapPatient(patientRequest);
        ProvidedTreatment providedTreatment = patient.getCurrentProvidedTreatment();
        assertEquals(0, providedTreatment.getWeightStatistics().size());
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
    public void newProviderTreatmentForCategoryChange_RetainsOldProviderIdAndAddress_SetsNewTbId_SetsNewTbRegistrationNumber_SetsNewTreatment_SetsStartDate() {
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

        assertNotSame(currentProvidedTreatment.getTbId().toLowerCase(), newProvidedTreatment.getTbId());
        assertNotSame(patient.latestTreatment(), newProvidedTreatment.getTreatment());
        assertEquals(openNewTreatmentUpdateRequest.getTb_id().toLowerCase(), newProvidedTreatment.getTbId());
        assertEquals(currentProvidedTreatment.getPatientAddress(), newProvidedTreatment.getPatientAddress());
        assertEquals(openNewTreatmentUpdateRequest.getProvider_id().toLowerCase(), newProvidedTreatment.getProviderId());
        //will set null if request does not have the field.
        assertEquals(openNewTreatmentUpdateRequest.getTb_registration_number(), newProvidedTreatment.getTbRegistrationNumber());
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id().toLowerCase(), patient.getPatientId());
        assertEquals(patientRequest.getFirst_name(), patient.getFirstName());
        assertEquals(patientRequest.getLast_name(), patient.getLastName());
        assertEquals(patientRequest.getGender(), patient.getGender());
        assertEquals(patientRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(patientRequest.getPhi(), patient.getPhi());
    }

    private void assertProvidedTreatment(Patient patient, PatientRequest patientRequest) {
        ProvidedTreatment providedTreatment = patient.getCurrentProvidedTreatment();
        assertEquals(patientRequest.getTb_id().toLowerCase(), providedTreatment.getTbId());
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

        assertEquals(patientRequest.getTreatmentCreationDate(), treatment.getCreationDate());
    }

    private void assertSmearTests(PatientRequest patientRequest, ProvidedTreatment treatment) {
        SmearTestRecord smearTestRecord = patientRequest.getSmearTestResults().get(0);
        assertEquals(smearTestRecord.getSmear_sample_instance(), treatment.getSmearTestResults().get(0).getSmear_sample_instance());
        assertEquals(smearTestRecord.getSmear_test_result_1(), treatment.getSmearTestResults().get(0).getSmear_test_result_1());
        assertEquals(smearTestRecord.getSmear_test_date_1(), treatment.getSmearTestResults().get(0).getSmear_test_date_1());
        assertEquals(smearTestRecord.getSmear_test_result_2(), treatment.getSmearTestResults().get(0).getSmear_test_result_2());
        assertEquals(smearTestRecord.getSmear_test_date_2(), treatment.getSmearTestResults().get(0).getSmear_test_date_2());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, ProvidedTreatment treatment) {
        assertEquals(patientRequest.getWeightStatistics(), treatment.getWeightStatistics());
    }
}
