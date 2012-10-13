package org.motechproject.whp.webservice.it.service;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.common.validation.RequestValidator;
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

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.now;

@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
public class PatientWebServiceIT extends SpringIntegrationTest {

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

    PatientWebService patientWebService;

    @Before
    public void setUpDefaultProvider() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        String defaultProviderId = patientWebRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @Before
    public void setUp() {
        patientWebService = new PatientWebService(patientService, validator, patientRequestMapper);
    }

    @Test
    public void shouldCreatePatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(patientWebRequest);
        assertNotNull(allPatients.findByPatientId(patientWebRequest.getCase_id()));
    }

    @Test
    public void migratedValueShouldBeFalseOnCreate() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(patientWebRequest);
        assertFalse(allPatients.findByPatientId(patientWebRequest.getCase_id()).isMigrated());
    }

    @Test
    public void shouldRecordTreatmentsWhenCreatingPatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();

        patientWebService.createCase(patientWebRequest);

        Patient recordedPatient = allPatients.findByPatientId(patientWebRequest.getCase_id());
        for (Treatment treatment : recordedPatient.getTreatmentHistory()) {
            assertNotNull(recordedPatient.getCurrentTherapy());
        }
    }

    @Test
    public void shouldUpdatePatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTbId("elevenDigit")
                .withCaseId("12341234")
                .build();
        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        PatientWebRequest simpleUpdateWebRequest = new PatientWebRequestBuilder().withSimpleUpdateFields()
                .withCaseId("12341234")
                .withTbId("elevenDigit")
                .build();
        patientWebService.updateCase(simpleUpdateWebRequest);

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

        //first closing current treatment
        PatientWebRequest closeRequest = new PatientWebRequestBuilder()
                .withDefaultsForCloseTreatment()
                .withTreatmentOutcome(TreatmentOutcome.TransferredOut.name())
                .build();
        patientWebService.updateCase(closeRequest);

        PatientWebRequest transferInRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withDate_Modified(dateModified)
                .build();
        Provider newProvider = new Provider(transferInRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);

        patientWebService.updateCase(transferInRequest);

        Patient updatedPatient = allPatients.findByPatientId(createPatientWebRequest.getCase_id());

        assertEquals(newProvider.getProviderId().toLowerCase(), updatedPatient.getCurrentTreatment().getProviderId());
        assertEquals(transferInRequest.getTb_id().toLowerCase(), updatedPatient.getCurrentTreatment().getTbId());
        assertEquals(dateModified.toLocalDate(), updatedPatient.getCurrentTreatment().getStartDate());
        assertEquals(patient.getCurrentTherapy().getUid(), updatedPatient.getCurrentTherapy().getUid());

        assertNotSame(patient.getCurrentTreatment().getProviderId(), updatedPatient.getCurrentTreatment().getProviderId());
        assertNotSame(patient.getCurrentTreatment().getTbId(), updatedPatient.getCurrentTreatment().getTbId());
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
        SampleInstance sampleInstance = SampleInstance.ExtendedIP;
        String labName = "Maxim";
        String labNumber = "11234556";
        PatientWebRequest simpleUpdateRequest = new PatientWebRequestBuilder()
                .withSimpleUpdateFields()
                .withSmearTestResults(sampleInstance.name(), resultDate, testResult.name(), resultDate, testResult.name(), labName, labNumber)
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.updateCase(simpleUpdateRequest);

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
        SampleInstance sampleInstance = SampleInstance.ExtendedIP;
        String labName = "Maxim";
        String labNumber = "11234556";
        PatientWebRequest simpleUpdateRequest = new PatientWebRequestBuilder()
                .withSimpleUpdateFields()
                .withSmearTestResults(sampleInstance.name(), resultDate, testResult.name(), resultDate, testResult.name(), labName, labNumber)
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();

        patientWebService.updateCase(simpleUpdateRequest);

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

        SampleInstance sampleInstance = SampleInstance.PreTreatment;
        PatientWebRequest simpleUpdateRequest = new PatientWebRequestBuilder()
                .withPatientAddress("new_house number", "new_landmark", "new_block", "new_village", "new_district", "new_state")
                .withSmearTestResults(sampleInstance.name(), "", "", "", "", "", "")
                .withWeightStatistics(SampleInstance.EndTreatment.name(), "99.7")
                .withTbId(tbId)
                .withCaseId(caseId)
                .build();
        patientWebRequest.setTreatmentData(null, simpleUpdateRequest.getTb_id(), null, null, "50", null);
        simpleUpdateRequest.setDate_modified("15/10/2010 10:10:10");
        simpleUpdateRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        simpleUpdateRequest.setPatientInfo(simpleUpdateRequest.getCase_id(), null, null, null, null, "9087654321", null);

        patientWebService.updateCase(simpleUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(caseId);

        SmearTestRecord smearTestRecord = updatedPatient.getTreatmentBy(tbId).getSmearTestResults().resultForInstance(sampleInstance);
        assertEquals(sampleInstance, smearTestRecord.getSmear_sample_instance());
        assertNull(smearTestRecord.getSmear_test_result_1());
        assertNull(smearTestRecord.getSmear_test_result_2());
        assertNull(smearTestRecord.getSmear_test_date_1());
        assertNull(smearTestRecord.getSmear_test_date_2());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }
}
