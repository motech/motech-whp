package org.motechproject.whp.it.remedi.inbound.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.District;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.repository.AllDistricts;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.validation.RequestValidator;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.*;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatmentCategories;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.user.domain.Provider;
import org.motechproject.whp.user.repository.AllProviders;
import org.motechproject.whp.user.service.ProviderService;
import org.motechproject.whp.webservice.builder.PatientWebRequestBuilder;
import org.motechproject.whp.webservice.mapper.PatientRequestMapper;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.motechproject.whp.webservice.service.PatientWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class PatientWebServiceIT extends SpringIntegrationTest {

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Autowired
    ProviderService providerService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    RequestValidator validator;
    @Autowired
    AllProviders allProviders;
    @Autowired
    AllTreatmentCategories allTreatmentCategories;
    @Autowired
    PatientService patientService;
    @Autowired
    PatientRequestMapper patientRequestMapper;
    @Autowired
    private AllDistricts allDistricts;

    @Autowired
    PatientWebService patientWebService;
    private District district;
    private String defaultProviderId;

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @Before
    public void setUp() {
        district = new District("district");
        allDistricts.add(district);
    }

    @Test
    public void shouldUpdatePatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();
        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        District new_district = new District("new_district");
        allDistricts.add(new_district);
        PatientWebRequest simpleUpdateWebRequest = new PatientWebRequestBuilder().withSimpleUpdateFields()
                .withCaseId("12341234")
                .withTbId("elevenDigit")
                .build();
        patientWebService.updateCase(simpleUpdateWebRequest);

        allDistricts.remove(new_district);
        Patient updatedPatient = allPatients.findByPatientId(simpleUpdateWebRequest.getCase_id());

        assertNotSame(patient.getPhoneNumber(), updatedPatient.getPhoneNumber());
        assertNotSame(patient.getCurrentTherapy(), updatedPatient.getCurrentTherapy());
    }

    @Test
    public void shouldNotSetMigratedOnUpdate() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();
        patientWebService.createCase(patientWebRequest);
        patientWebService.updateCase(patientWebRequest);
        Patient updatedPatient = allPatients.findByPatientId(patientWebRequest.getCase_id());
        assertFalse(updatedPatient.isMigrated());

    }

    @Test
    public void shouldUpdatePatientsProvider_WhenTreatmentUpdateTypeIsTransferIn() {
        //For the mapping to take place [allTreatmentCategories.findByCode()]
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));

        PatientWebRequest createPatientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(createPatientWebRequest);
        Patient patient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        DateTime dateModified = DateUtil.now();
        DateTime tbRegistrationDate = new DateTime(2012, 11, 11, 0, 0, 0);

        //first closing current treatment
        PatientWebRequest closeRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();
        patientWebService.updateCase(closeRequest);

        PatientWebRequest transferInRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withDate_Modified(dateModified)
                .withTbRegistartionDate(tbRegistrationDate)
                .build();
        Provider newProvider = new Provider(transferInRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);

        patientWebService.updateCase(transferInRequest);

        Patient updatedPatient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        assertEquals(newProvider.getProviderId().toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(transferInRequest.getTb_id().toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
        assertEquals(tbRegistrationDate.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
        assertEquals(patient.getCurrentTherapy().getUid(), updatedPatient.getCurrentTherapy().getUid());

        assertNotSame(patient.getCurrentTreatment().getProviderId(), updatedPatient.getCurrentTreatment().getProviderId());
        assertNotSame(patient.getCurrentTreatment().getTbId(), updatedPatient.getCurrentTreatment().getTbId());

        assertTreatmentDetails(transferInRequest, updatedPatient.getCurrentTreatment().getTreatmentDetails());
    }

    @Test
    public void shouldUpdatePatientsProvider_WhenTreatmentUpdateTypeIsNewTreatment() {
        List<DayOfWeek> threeDaysAWeek = Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday);
        allTreatmentCategories.add(new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 24, 4, 12, 18, 54, threeDaysAWeek));

        PatientWebRequest createPatientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(createPatientWebRequest);
        Patient patient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        DateTime dateModified = DateUtil.now();
        DateTime tbRegistrationDate = new DateTime(2012, 11, 11, 0, 0, 0);

        //first closing current treatment
        PatientWebRequest closeRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();
        patientWebService.updateCase(closeRequest);

        PatientWebRequest newTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForNewTreatment()
                .withDate_Modified(dateModified)
                .withTbRegistartionDate(tbRegistrationDate)
                .build();
        Provider newProvider = new Provider(newTreatmentRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);

        patientWebService.updateCase(newTreatmentRequest);

        Patient updatedPatient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        assertEquals(newProvider.getProviderId().toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(newTreatmentRequest.getTb_id().toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
        assertEquals(tbRegistrationDate.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());

        assertNotSame(patient.getCurrentTreatment().getProviderId(), updatedPatient.getCurrentTreatment().getProviderId());
        assertNotSame(patient.getCurrentTreatment().getTbId(), updatedPatient.getCurrentTreatment().getTbId());

        assertTreatmentDetails(newTreatmentRequest, updatedPatient.getCurrentTreatment().getTreatmentDetails());
    }


    @Test
    public void shouldUpdatePatientTreatment() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();
        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        patientWebRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();
        patientWebService.updateCase(patientWebRequest);

        Patient updatedPatient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        assertNotSame(patient.getLastModifiedDate(), updatedPatient.getLastModifiedDate());
        assertNotSame(patient.getCurrentTreatment().getEndDate(), updatedPatient.getCurrentTreatment().getEndDate());
        assertNotSame(patient.getCurrentTherapy(), updatedPatient.getCurrentTherapy());
    }

    @Test
    public void shouldPauseAndRestartPatientTreatment() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();

        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());
        assertFalse(patient.getCurrentTreatment().isPaused());

        PatientWebRequest pauseTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForPauseTreatment()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();

        patientWebService.updateCase(pauseTreatmentRequest);

        Patient updatedPatient = allPatients.findByPatientId(pauseTreatmentRequest.getCase_id());

        assertTrue(updatedPatient.getCurrentTreatment().isPaused());

        PatientWebRequest restartTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForRestartTreatment()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();

        patientWebService.updateCase(restartTreatmentRequest);

        updatedPatient = allPatients.findByPatientId(pauseTreatmentRequest.getCase_id());

        assertFalse(updatedPatient.getCurrentTreatment().isPaused());
    }

    @Test
    public void shouldCloseTreatment() {
        // Creating a patient
        String caseId = "12341234";
        String tbId = "elevendigit";
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.createCase(patientWebRequest);

        // Closing current treatment
        String remarks = "remarks";
        PatientWebRequest closeTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTbId(tbId)
                .withCaseId(caseId).withTreatmentOutcome(TreatmentOutcome.Cured.name())
                .withRemarks(remarks)
                .build();

        patientWebService.updateCase(closeTreatmentRequest);

        Patient updatedPatient = allPatients.findByPatientId(caseId);
        assertThat(updatedPatient.getCurrentTreatment().getCloseTreatmentRemarks(), is(remarks));
        assertThat(updatedPatient.getCurrentTreatment().getTbId(), is(closeTreatmentRequest.getTb_id()));
        assertThat(updatedPatient.getCurrentTreatment().getTreatmentOutcome(), is(TreatmentOutcome.Cured));
    }


    @Test
    public void shouldPerformSimpleUpdateOnClosedTreatment() {
        // Creating a patient
        String caseId = "12341234";
        String tbId = "elevenDigit";
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.createCase(patientWebRequest);

        // Closing current treatment
        PatientWebRequest closeTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTbId(tbId)
                .withCaseId(caseId).withTreatmentOutcome(TreatmentOutcome.Cured.name())
                .build();

        patientWebService.updateCase(closeTreatmentRequest);

        // Updating current closed treatment
        String resultDate = "04/09/2012";
        SmearTestResult testResult = SmearTestResult.Positive;
        SputumTrackingInstance sampleInstance = SputumTrackingInstance.ExtendedIP;
        String labName = "Maxim";
        String labNumber = "11234556";
        District new_district = new District("new_district");
        allDistricts.add(new_district);
        PatientWebRequest simpleUpdateRequest = new PatientWebRequestBuilder()
                .withSimpleUpdateFields()
                .withSmearTestResults(sampleInstance.name(), resultDate, testResult.name(), resultDate, testResult.name(), labName, labNumber)
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.updateCase(simpleUpdateRequest);

        allDistricts.remove(new_district);
        Patient updatedPatient = allPatients.findByPatientId(caseId);

        SmearTestRecord smearTestRecord = updatedPatient.getCurrentTreatment().getSmearTestResults().resultForInstance(sampleInstance);
        assertEquals(sampleInstance, smearTestRecord.getSmear_sample_instance());
        assertEquals(testResult, smearTestRecord.getSmear_test_result_1());
        assertEquals(testResult, smearTestRecord.getSmear_test_result_2());
        assertEquals(labName, smearTestRecord.getLabName());
        assertEquals(labNumber, smearTestRecord.getLabNumber());
    }

    @Test
    public void shouldPerformSimpleUpdateOnHistorisedTreatment() {
        // Creating a patient
        String caseId = "12341234";
        String tbId = "elevenDigit";
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.createCase(patientWebRequest);

        // Closing current treatment by transferring patient out
        PatientWebRequest closeTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTbId(tbId)
                .withCaseId(caseId).withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();

        patientWebService.updateCase(closeTreatmentRequest);

        // Opening a new treatment by transferring patient in
        PatientWebRequest transferInRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withCaseId(caseId)
                .withDate_Modified(now())
                .build();
        Provider newProvider = new Provider(transferInRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);
        patientWebService.updateCase(transferInRequest);

        // Updating historised treatment
        String resultDate = "04/09/2012";
        SmearTestResult testResult = SmearTestResult.Positive;
        SputumTrackingInstance sampleInstance = SputumTrackingInstance.ExtendedIP;
        String labName = "Maxim";
        String labNumber = "11234556";
        PatientWebRequest simpleUpdateRequest = new PatientWebRequestBuilder()
                .withSimpleUpdateFields()
                .withSmearTestResults(sampleInstance.name(), resultDate, testResult.name(), resultDate, testResult.name(), labName, labNumber)
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();
        District new_district = new District("new_district");
        allDistricts.add(new_district);

        patientWebService.updateCase(simpleUpdateRequest);

        allDistricts.remove(new_district);
        Patient updatedPatient = allPatients.findByPatientId(caseId);

        SmearTestRecord smearTestRecord = updatedPatient.getTreatmentBy(tbId).getSmearTestResults().resultForInstance(sampleInstance);
        assertEquals(sampleInstance, smearTestRecord.getSmear_sample_instance());
        assertEquals(testResult, smearTestRecord.getSmear_test_result_1());
        assertEquals(testResult, smearTestRecord.getSmear_test_result_2());
        assertEquals(labName, smearTestRecord.getLabName());
        assertEquals(labNumber, smearTestRecord.getLabNumber());
    }

    @Test
    public void shouldPerformSimpleUpdateForEmptySmearTestResults() {
        // Creating a patient
        String caseId = "12341234";
        String tbId = "elevenDigit";
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.createCase(patientWebRequest);

        SputumTrackingInstance sampleInstance = SputumTrackingInstance.PreTreatment;
        PatientWebRequest simpleUpdateRequest = new PatientWebRequestBuilder()
                .withPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .withSmearTestResults(sampleInstance.name(), "", "", "", "", "", "")
                .withWeightStatistics(SputumTrackingInstance.EndTreatment.name(), "99.7")
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();
        patientWebRequest.setTreatmentData(null, simpleUpdateRequest.getTb_id(), null, null, "50", null);
        simpleUpdateRequest.setDate_modified("15/10/2010 10:10:10");
        simpleUpdateRequest.setTb_registration_date("17/10/2010 10:10:10");
        simpleUpdateRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        simpleUpdateRequest.setPatientInfo(simpleUpdateRequest.getCase_id(), null, null, null, null, "9087654321", null);
        District new_district = new District("new_district");
        allDistricts.add(new_district);

        patientWebService.updateCase(simpleUpdateRequest);

        allDistricts.remove(new_district);
        Patient updatedPatient = allPatients.findByPatientId(caseId);

        SmearTestRecord smearTestRecord = updatedPatient.getTreatmentBy(tbId).getSmearTestResults().get(0);
        assertEquals(sampleInstance, smearTestRecord.getSmear_sample_instance());
        assertNull(smearTestRecord.getSmear_test_result_1());
        assertNull(smearTestRecord.getSmear_test_result_2());
        assertNull(smearTestRecord.getSmear_test_date_1());
        assertNull(smearTestRecord.getSmear_test_date_2());
    }

    @Test
    public void patientCanBeTransferredToAnotherProvider_WhenHeWasTransferredOutForPreviousTreatment() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        PatientRequest closeRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTreatmentOutcome(TreatmentOutcome.TransferredOut).build();
        patientWebService.update(closeRequest);

        assertTrue(patientWebService.canBeTransferred(createPatientRequest.getCase_id()));
    }

    @Test
    public void patientCannotBeTransferredToAnotherProvider_WhenHeWasNotTransferredOutForPreviousTreatment() {
        PatientRequest createPatientRequest = new PatientRequestBuilder().withDefaults().build();
        patientService.createPatient(createPatientRequest);
        PatientRequest closeRequest = new PatientRequestBuilder().withMandatoryFieldsForCloseTreatment().withTreatmentOutcome(TreatmentOutcome.Died).build();
        patientWebService.update(closeRequest);

        assertFalse(patientWebService.canBeTransferred(createPatientRequest.getCase_id()));
    }

    public static void assertTreatmentDetails(PatientWebRequest patientRequest, TreatmentDetails treatmentDetails){
        assertEquals(patientRequest.getDistrict_with_code(), treatmentDetails.getDistrictWithCode());
        assertEquals(patientRequest.getTb_unit_with_code(), treatmentDetails.getTbUnitWithCode());
        assertEquals(patientRequest.getEp_site(), treatmentDetails.getEpSite());
        assertEquals(patientRequest.getOther_investigations(), treatmentDetails.getOtherInvestigations());
        assertEquals(patientRequest.getPrevious_treatment_history(), treatmentDetails.getPreviousTreatmentHistory());
        assertEquals(patientRequest.getHiv_status(), treatmentDetails.getHivStatus());

        if(patientRequest.getHiv_test_date() != null){
            assertEquals(patientRequest.getHiv_test_date(), treatmentDetails.getHivTestDate().toString(WHPDate.DATE_FORMAT));
        } else {
            assertNull(treatmentDetails.getHivTestDate());
        }

        assertEquals(Integer.valueOf(patientRequest.getMembers_below_six_years()), treatmentDetails.getMembersBelowSixYears());
        assertEquals(patientRequest.getPhc_referred() , treatmentDetails.getPhcReferred());
        assertEquals(patientRequest.getProvider_name(), treatmentDetails.getProviderName());
        assertEquals(patientRequest.getDot_centre(), treatmentDetails.getDotCentre());
        assertEquals(patientRequest.getProvider_type(), treatmentDetails.getProviderType());
        assertEquals(patientRequest.getCmf_doctor(), treatmentDetails.getCmfDoctor());
        assertEquals(patientRequest.getContact_person_name(), treatmentDetails.getContactPersonName());
        assertEquals(patientRequest.getContact_person_phone_number() , treatmentDetails.getContactPersonPhoneNumber());
        assertEquals(patientRequest.getXpert_test_result() , treatmentDetails.getXpertTestResult());
        assertEquals(patientRequest.getXpert_device_number() , treatmentDetails.getXpertDeviceNumber());

        if(patientRequest.getXpert_test_date() != null)
            assertEquals(patientRequest.getXpert_test_date() , treatmentDetails.getXpertTestDate().toString(WHPDate.DATE_FORMAT));
        else
            assertNull(treatmentDetails.getXpertTestDate());

        assertEquals(patientRequest.getRif_resistance_result(), treatmentDetails.getRifResistanceResult());
    }


    @After
    public void tearDown() {
        allDistricts.remove(district);

        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }


}
