package org.motechproject.whp.patient.mapper;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.SmearTestRecord;
import org.motechproject.whp.patient.domain.Therapy;
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
        assertTreatment(patient, patientRequest);
    }

    @Test
    public void shouldMapWeightStatisticsAsEmpty_WhenMissing() {
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForImportPatient()
                .withWeightStatistics(null, null, DateUtil.today())
                .build();
        Patient patient = mapPatient(patientRequest);
        Treatment treatment = patient.getCurrentTreatment();
        assertEquals(0, treatment.getWeightStatistics().size());
    }

    @Test
    public void mapTreatmentSetsStartDateOnTreatment() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withCaseId("caseId")
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        Therapy therapy = TherapyMapper.map(patientRequest);
        Treatment treatment = mapTreatment(patientRequest, therapy);

        assertNotNull(treatment.getStartDate());
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

        Treatment currentTreatment = patient.getCurrentTreatment();

        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForOpenNewTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .build();

        Therapy newTherapy = TherapyMapper.createNewTreatment(patient, openNewTreatmentUpdateRequest);

        Treatment newTreatment = createNewTreatmentForTreatmentCategoryChange(patient, openNewTreatmentUpdateRequest, newTherapy);

        assertNotSame(currentTreatment.getTbId().toLowerCase(), newTreatment.getTbId());
        assertNotSame(patient.latestTherapy(), newTreatment.getTherapy());
        assertEquals(openNewTreatmentUpdateRequest.getTb_id().toLowerCase(), newTreatment.getTbId());
        assertEquals(currentTreatment.getPatientAddress(), newTreatment.getPatientAddress());
        assertEquals(openNewTreatmentUpdateRequest.getProvider_id().toLowerCase(), newTreatment.getProviderId());
        //will set null if request does not have the field.
        assertEquals(openNewTreatmentUpdateRequest.getTb_registration_number(), newTreatment.getTbRegistrationNumber());
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id().toLowerCase(), patient.getPatientId());
        assertEquals(patientRequest.getFirst_name(), patient.getFirstName());
        assertEquals(patientRequest.getLast_name(), patient.getLastName());
        assertEquals(patientRequest.getGender(), patient.getGender());
        assertEquals(patientRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(patientRequest.getPhi(), patient.getPhi());
    }

    private void assertTreatment(Patient patient, PatientRequest patientRequest) {
        Treatment treatment = patient.getCurrentTreatment();
        assertEquals(patientRequest.getTb_id().toLowerCase(), treatment.getTbId());
        assertEquals(patientRequest.getProvider_id(), treatment.getProviderId());
        assertEquals(patientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(patientRequest.getAddress(), treatment.getPatientAddress());

        assertTherapy(patient, patientRequest);
        assertSmearTests(patientRequest, patient.getCurrentTreatment());
        assertWeightStatistics(patientRequest, patient.getCurrentTreatment());
    }

    private void assertTherapy(Patient patient, PatientRequest patientRequest) {
        Therapy therapy = patient.latestTherapy();
        assertEquals(patientRequest.getAge(), therapy.getPatientAge());
        assertEquals(patientRequest.getTreatment_category(), therapy.getTreatmentCategory());
        assertNull(therapy.getStartDate());

        assertEquals(patientRequest.getTreatmentCreationDate(), therapy.getCreationDate());
    }

    private void assertSmearTests(PatientRequest patientRequest, Treatment treatment) {
        SmearTestRecord smearTestRecord = patientRequest.getSmearTestResults().get(0);
        assertEquals(smearTestRecord.getSmear_sample_instance(), treatment.getSmearTestResults().get(0).getSmear_sample_instance());
        assertEquals(smearTestRecord.getSmear_test_result_1(), treatment.getSmearTestResults().get(0).getSmear_test_result_1());
        assertEquals(smearTestRecord.getSmear_test_date_1(), treatment.getSmearTestResults().get(0).getSmear_test_date_1());
        assertEquals(smearTestRecord.getSmear_test_result_2(), treatment.getSmearTestResults().get(0).getSmear_test_result_2());
        assertEquals(smearTestRecord.getSmear_test_date_2(), treatment.getSmearTestResults().get(0).getSmear_test_date_2());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        assertEquals(patientRequest.getWeightStatistics(), treatment.getWeightStatistics());
    }
}
