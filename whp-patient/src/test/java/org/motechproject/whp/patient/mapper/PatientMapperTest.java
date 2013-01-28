package org.motechproject.whp.patient.mapper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.user.builder.ProviderBuilder;
import org.motechproject.whp.user.service.ProviderService;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.builder.PatientRequestBuilder.NEW_PROVIDER_ID;

public class PatientMapperTest {

    private PatientMapper patientMapper;

    @Mock
    private ProviderService providerService;
    private String providerId = "provider-id";
    private String providerDistrict;

    @Before
    public void setUp() {
        initMocks(this);
        patientMapper = new PatientMapper(providerService);
        providerDistrict = "district";
        when(providerService.findByProviderId(providerId)).thenReturn(new ProviderBuilder()
                .withDefaults()
                .withProviderId(providerId)
                .withDistrict(providerDistrict)
                .build());
    }

    @Test
    public void shouldMapPatientRequestToPatientDomain() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withProviderId(providerId)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .build();
        Patient patient = patientMapper.mapPatient(patientRequest);
        assertBasicPatientInfo(patient, patientRequest);
        assertTreatment(patientRequest, patientRequest.getAddress(), patient.getCurrentTreatment());
        assertTherapy(patientRequest, patientRequest.getAge(), patient.getCurrentTherapy());
        assertThat(patient.getCurrentTreatment().getProviderDistrict(), is(providerDistrict));
        verify(providerService).findByProviderId(providerId);
    }

    @Test
    public void shouldMapWeightStatisticsAsEmpty_WhenMissing() {
        PatientRequest patientRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForImportPatient()
                .withWeightStatistics(null, null, DateUtil.today())
                .withProviderId(providerId)
                .build();
        Patient patient = patientMapper.mapPatient(patientRequest);
        Treatment treatment = patient.getCurrentTreatment();
        assertEquals(0, treatment.getWeightStatistics().size());
        verify(providerService).findByProviderId(providerId);
    }

    @Test
    public void mapIsMigratedDetail() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withProviderId(providerId).build();
        patientRequest.setMigrated(true);
        Patient patient = patientMapper.mapPatient(patientRequest);
        assertTrue(patient.isMigrated());

        patientRequest.setMigrated(false);
        patient = patientMapper.mapPatient(patientRequest);
        assertFalse(patient.isMigrated());

        verify(providerService, times(2)).findByProviderId(providerId);
    }


    @Test
    public void shouldCreateNewTreatmentForCategoryChange() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withProviderId(providerId)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .build();
        Patient patient = patientMapper.mapPatient(patientRequest);

        Therapy oldTherapy = patient.getCurrentTherapy();
        Treatment oldTreatment = patient.getCurrentTreatment();

        String newProviderId = "newproviderid";
        PatientRequest openNewTreatmentUpdateRequest = new PatientRequestBuilder()
                .withProviderId(providerId)
                .withMandatoryFieldsForOpenNewTreatment()
                .withProviderId(newProviderId)
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .withPatientAge(60)
                .withPatientType(PatientType.Relapse)
                .build();

        String newProviderDistrictName = "new-district";
        when(providerService.findByProviderId(newProviderId)).thenReturn(new ProviderBuilder()
                .withDefaults()
                .withProviderId(newProviderId)
                .withDistrict(newProviderDistrictName)
                .build());


        patientMapper.mapNewTreatmentForCategoryChange(openNewTreatmentUpdateRequest, patient);

        assertNotSame(patient.getCurrentTherapy(), oldTherapy);

        assertTherapy(openNewTreatmentUpdateRequest, patient.getAge(), patient.getCurrentTherapy());
        assertTreatment(openNewTreatmentUpdateRequest, oldTreatment.getPatientAddress(), patient.getCurrentTreatment());
        assertThat(patient.getCurrentTreatment().getProviderDistrict(), is(newProviderDistrictName));
        verify(providerService).findByProviderId(providerId);
        verify(providerService).findByProviderId(newProviderId);
    }

    @Test
    public void shouldCreateNewTreatmentForTransferIn() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withProviderId(providerId)
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .build();
        Patient patient = patientMapper.mapPatient(patientRequest);
        verify(providerService).findByProviderId(providerId);

        Therapy therapy = patient.getCurrentTherapy();
        Treatment oldTreatment = patient.getCurrentTreatment();

        PatientRequest transferInRequest = new PatientRequestBuilder()
                .withMandatoryFieldsForTransferInTreatment()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTbId("newTbId")
                .withPatientAge(60)
                .withProviderId(NEW_PROVIDER_ID)
                .withDiseaseClass(DiseaseClass.E)
                .build();

        String newDistrictName = "new-district";
        when(providerService.findByProviderId(NEW_PROVIDER_ID)).thenReturn(new ProviderBuilder()
                .withDefaults()
                .withProviderId(NEW_PROVIDER_ID)
                .withDistrict(newDistrictName)
                .build());

        patientMapper.mapTreatmentForTransferIn(transferInRequest, patient);

        assertSame(therapy, patient.getCurrentTherapy());
        assertTreatment(transferInRequest, oldTreatment.getPatientAddress(), patient.getCurrentTreatment());
        assertEquals(DiseaseClass.E, patient.getCurrentTherapy().getDiseaseClass());
        verify(providerService).findByProviderId(NEW_PROVIDER_ID);
        assertThat(patient.getCurrentTreatment().getProviderDistrict(), is(newDistrictName));
    }

    @Test
    public void shouldUpdatePatientInformation() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withProviderId(providerId)
                .build();
        Patient patient = patientMapper.mapPatient(patientRequest);

        verify(providerService).findByProviderId(providerId);

        PatientRequest updateRequest = new PatientRequestBuilder()
                .withSimpleUpdateFields()
                .withDateModified(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(60)
                .build();

        patientMapper.mapUpdates(updateRequest, patient);

        Therapy therapy = patient.getCurrentTherapy();
        Treatment treatment = patient.getCurrentTreatment();

        assertEquals(updateRequest.getTb_registration_date().toLocalDate(), treatment.getStartDate());
        assertEquals(updateRequest.getDate_modified(), patient.getLastModifiedDate());
        assertEquals(updateRequest.getAddress(), treatment.getPatientAddress());
        assertEquals(updateRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(updateRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(updateRequest.getAge(), therapy.getPatientAge());

        assertEquals(updateRequest.getSmearTestResults().size() + 1, treatment.getSmearTestResults().size());
        assertSmearTestResult(updateRequest.getSmearTestResults().get(0), treatment.getSmearTestResults().get(1));

        assertEquals(updateRequest.getWeightStatistics().size() + 1, treatment.getWeightStatistics().size());
        assertEquals(updateRequest.getWeightStatistics().get(0), treatment.getWeightStatistics().get(1));
    }

    @Test
    public void shouldSetProviderDistrictOnCreatingNewTreatment(){
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withPatientAge(50)
                .withProviderId(providerId)
                .build();

        Treatment treatment = patientMapper.createTreatment(patientRequest, patientRequest.getAddress());

        assertThat(treatment.getProviderDistrict(), is(providerDistrict));
        verify(providerService).findByProviderId(providerId);
    }


    @After
    public void tearDown() {
        verifyNoMoreInteractions(providerService);
    }

    private void assertBasicPatientInfo(Patient patient, PatientRequest patientRequest) {
        assertEquals(patientRequest.getCase_id().toLowerCase(), patient.getPatientId());
        assertEquals(patientRequest.getFirst_name(), patient.getFirstName());
        assertEquals(patientRequest.getLast_name(), patient.getLastName());
        assertEquals(patientRequest.getGender(), patient.getGender());
        assertEquals(patientRequest.getMobile_number(), patient.getPhoneNumber());
        assertEquals(patientRequest.getPhi(), patient.getPhi());
    }

    private void assertTreatment(PatientRequest patientRequest, Address address, Treatment treatment) {
        assertEquals(patientRequest.getTb_registration_date().toLocalDate(), treatment.getStartDate());
        assertEquals(patientRequest.getTb_id().toLowerCase(), treatment.getTbId());
        assertEquals(patientRequest.getProvider_id(), treatment.getProviderId());
        assertEquals(patientRequest.getTb_registration_number(), treatment.getTbRegistrationNumber());
        assertEquals(patientRequest.getPatient_type(), treatment.getPatientType());
        assertEquals(address, treatment.getPatientAddress());

        assertSmearTests(patientRequest, treatment);
        assertWeightStatistics(patientRequest, treatment);
    }

    private void assertTherapy(PatientRequest patientRequest, Integer expectedPatientAge, Therapy therapy) {
        assertEquals(expectedPatientAge, therapy.getPatientAge());
        assertEquals(patientRequest.getTreatment_category(), therapy.getTreatmentCategory());
        assertEquals(patientRequest.getDisease_class(), therapy.getDiseaseClass());
        assertNull(therapy.getStartDate());

        assertEquals(patientRequest.getTreatmentCreationDate(), therapy.getCreationDate());
    }

    private void assertSmearTests(PatientRequest patientRequest, Treatment treatment) {
        if(patientRequest.getSmearTestResults().isEmpty()) return;
        SmearTestRecord expectedSmearTestRecord = patientRequest.getSmearTestResults().get(0);
        assertEquals(expectedSmearTestRecord.getSmear_sample_instance(), treatment.getSmearTestResults().get(0).getSmear_sample_instance());
        assertSmearTestResult(expectedSmearTestRecord, treatment.getSmearTestResults().get(0));
    }

    private void assertSmearTestResult(SmearTestRecord expected, SmearTestRecord actual) {
        assertEquals(expected.getSmear_test_result_1(), actual.getSmear_test_result_1());
        assertEquals(expected.getSmear_test_date_1(), actual.getSmear_test_date_1());
        assertEquals(expected.getSmear_test_result_2(), actual.getSmear_test_result_2());
        assertEquals(expected.getSmear_test_date_2(), actual.getSmear_test_date_2());
    }

    private void assertWeightStatistics(PatientRequest patientRequest, Treatment treatment) {
        assertEquals(patientRequest.getWeightStatistics(), treatment.getWeightStatistics());
    }
}
