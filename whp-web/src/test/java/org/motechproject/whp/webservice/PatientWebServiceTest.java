package org.motechproject.whp.webservice;

import org.dozer.DozerBeanMapper;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.registration.service.RegistrationService;
import org.motechproject.whp.builder.PatientWebRequestBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.request.PatientWebRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.*;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientWebServiceTest extends SpringIntegrationTest {

    @Autowired
    RegistrationService patientRegistrationService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;
    @Autowired
    RequestValidator validator;
    @Autowired
    AllProviders allProviders;
    @Autowired
    PatientService patientService;
    @Autowired
    DozerBeanMapper patientRequestMapper;
    @Autowired
    DozerBeanMapper treatmentUpdateRequestMapper;

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
        patientWebService = new PatientWebService(patientRegistrationService, patientService, validator, patientRequestMapper, treatmentUpdateRequestMapper);
    }

    @Test
    public void shouldCreatePatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(patientWebRequest);
        assertNotNull(allPatients.findByPatientId(patientWebRequest.getCase_id()));
    }

    @Test
    public void shouldRecordProvidedTreatmentsWhenCreatingPatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();

        patientWebService.createCase(patientWebRequest);

        Patient recordedPatient = allPatients.findByPatientId(patientWebRequest.getCase_id());
        for (ProvidedTreatment providedTreatment : recordedPatient.getProvidedTreatments()) {
            assertNotNull(providedTreatment.getTreatment());
        }
    }

    @Test
    public void shouldUpdatePatient() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                                                                            .withTBId("elevenDigit")
                                                                            .withCaseId("12341234")
                                                                            .build();
        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        PatientWebRequest simpleUpdateWebRequest = new PatientWebRequestBuilder().withSimpleUpdateFields()
                                                                                 .withCaseId("12341234")
                                                                                 .withTBId("elevenDigit")
                                                                                 .build();
        patientWebService.updateCase(simpleUpdateWebRequest);

        Patient updatedPatient = allPatients.findByPatientId(simpleUpdateWebRequest.getCase_id());

        assertNotSame(patient.getPhoneNumber(), updatedPatient.getPhoneNumber());
        assertNotSame(patient.getCurrentProvidedTreatment().getTreatment(), updatedPatient.getCurrentProvidedTreatment().getTreatment());
    }

    @Test
    public void shouldUpdatePatientsProvider_WhenTreatmentUpdateTypeIsTransferIn() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults().build();
        patientWebService.createCase(patientWebRequest);
        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        DateTime dateModified = DateUtil.now();
        PatientWebRequest treatmentUpdateRequest = new PatientWebRequestBuilder()
                .withDefaultsForTransferIn()
                .withDate_Modified(dateModified)
                .withCaseId(patientWebRequest.getCase_id())
                .withOldTb_Id(patient.getCurrentProvidedTreatment().getTbId())
                .build();
        Provider newProvider = new Provider(treatmentUpdateRequest.getProvider_id(), "1234567890", "chambal", DateUtil.now());
        allProviders.add(newProvider);

        patientWebService.updateCase(treatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());

        assertEquals(treatmentUpdateRequest.getProvider_id(), updatedPatient.getCurrentProvidedTreatment().getProviderId());
        assertEquals(treatmentUpdateRequest.getTb_id(), updatedPatient.getCurrentProvidedTreatment().getTbId());
        assertEquals(dateModified.toLocalDate(), updatedPatient.getCurrentProvidedTreatment().getStartDate());

        assertNotSame(patient.getCurrentProvidedTreatment().getProviderId(), updatedPatient.getCurrentProvidedTreatment().getProviderId());
        assertNotSame(patient.getCurrentProvidedTreatment().getTbId(), updatedPatient.getCurrentProvidedTreatment().getTbId());
    }

    @Test
    public void shouldUpdatePatientTreatment() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                                                                            .withTBId("elevenDigit")
                                                                            .withCaseId("12341234")
                                                                            .build();
        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());

        PatientWebRequest treatmentUpdateRequest = new PatientWebRequestBuilder().withOnlyRequiredTreatmentUpdateFields()
                                                                                 .withTBId("elevenDigit")
                                                                                 .withCaseId("12341234")
                                                                                 .build();
        patientWebService.updateCase(treatmentUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(treatmentUpdateRequest.getCase_id());

        assertNotSame(patient.getLastModifiedDate(), updatedPatient.getLastModifiedDate());
        assertNotSame(patient.getCurrentProvidedTreatment().getEndDate(), updatedPatient.getCurrentProvidedTreatment().getEndDate());
        assertNotSame(patient.getCurrentProvidedTreatment().getTreatment(), updatedPatient.getCurrentProvidedTreatment().getTreatment());
    }

    @Test
    public void shouldPauseAndRestartPatientTreatment() {
        PatientWebRequest patientWebRequest = new PatientWebRequestBuilder().withDefaults()
                .withTBId("elevenDigit")
                .withCaseId("12341234")
                .build();

        patientWebService.createCase(patientWebRequest);

        Patient patient = allPatients.findByPatientId(patientWebRequest.getCase_id());
        assertFalse(patient.getCurrentProvidedTreatment().isPaused());

        PatientWebRequest pauseTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForPauseTreatment()
                .withTBId("elevenDigit")
                .withCaseId("12341234")
                .build();

        patientWebService.updateCase(pauseTreatmentRequest);

        Patient updatedPatient = allPatients.findByPatientId(pauseTreatmentRequest.getCase_id());

        assertTrue(updatedPatient.getCurrentProvidedTreatment().isPaused());

        PatientWebRequest restartTreatmentRequest = new PatientWebRequestBuilder()
                .withDefaultsForRestartTreatment()
                .withTBId("elevenDigit")
                .withCaseId("12341234")
                .build();

        patientWebService.updateCase(restartTreatmentRequest);

        updatedPatient = allPatients.findByPatientId(pauseTreatmentRequest.getCase_id());

        assertFalse(updatedPatient.getCurrentProvidedTreatment().isPaused());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }
}
