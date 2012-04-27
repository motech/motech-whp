package org.motechproject.whp.webservice;

import org.apache.velocity.app.VelocityEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.application.service.RegistrationService;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.domain.Provider;
import org.motechproject.whp.patient.exception.WHPException;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllProviders;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.patient.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientWebServiceIT extends SpringIntegrationTest {

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
    VelocityEngine velocityEngine;
    @Autowired
    private PatientService patientService;

    PatientWebService patientWebService;


    @Before
    public void setUpDefaultProvider() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        String defaultProviderId = patientRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @Before
    public void setUp() {
        patientWebService = new PatientWebService(patientRegistrationService, patientService, allTreatments, validator, velocityEngine);
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientWebService.createCase(patientRequest);
        assertNotNull(allPatients.findByPatientId(patientRequest.getCase_id()));
    }

    @Test
    public void shouldRecordProvidedTreatmentsWhenCreatingPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();

        patientWebService.createCase(patientRequest);

        Patient recordedPatient = allPatients.findByPatientId(patientRequest.getCase_id());
        for (ProvidedTreatment providedTreatment : recordedPatient.getProvidedTreatments()) {
            assertNotNull(providedTreatment.getTreatment());
        }
    }

    @Test(expected = WHPException.class)
    public void shouldNotCreatePatientWhenProviderIdIsInvalid() {
        String unknownProviderId = "012900";
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientRequest.setProvider_id(unknownProviderId);
        patientWebService.createCase(patientRequest);
    }

    @Test
    public void shouldUpdatePatient(){
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().withCaseId("12341234").build();
        patientWebService.createCase(patientRequest);

        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        PatientRequest simpleUpdateRequest = new PatientRequestBuilder().withSimpleUpdateFields().withCaseId("12341234").build();
        patientWebService.updateCase(simpleUpdateRequest);

        Patient updatedPatient = allPatients.findByPatientId(simpleUpdateRequest.getCase_id());

        assertNotSame(patient.getPhoneNumber(), updatedPatient.getPhoneNumber());
        assertNotSame(patient.getCurrentProvidedTreatment().getTreatment(), updatedPatient.getCurrentProvidedTreatment().getTreatment());
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }
}
