package org.motechproject.whp.webservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.application.service.PatientRegistrationService;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.domain.ProvidedTreatment;
import org.motechproject.whp.domain.Provider;
import org.motechproject.whp.exception.WHPException;
import org.motechproject.whp.repository.AllPatients;
import org.motechproject.whp.repository.AllProviders;
import org.motechproject.whp.repository.AllTreatments;
import org.motechproject.whp.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertNotNull;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientWebServiceIT extends SpringIntegrationTest {

    @Autowired
    PatientRegistrationService patientRegistrationService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;
    @Autowired
    RequestValidator validator;
    @Autowired
    AllProviders allProviders;

    private PatientWebService patientWebService;

    @Before
    public void setUpDefaultProvider() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        String defaultProviderId = patientRequest.getProvider_id();
        Provider defaultProvider = new Provider(defaultProviderId, "1234567890", "chambal", DateUtil.now());
        allProviders.add(defaultProvider);
    }

    @Before
    public void setUp() {
        patientWebService = new PatientWebService(patientRegistrationService, allTreatments, validator);
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();
        patientWebService.createCase(patientRequest);
        assertNotNull(allPatients.findByPatientId(patientRequest.getCase_id()));
    }

    @Test
    public void shouldRecordLastProvidedTreatmentWhenCreatingPatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();

        patientWebService.createCase(patientRequest);

        Patient recordedPatient = allPatients.findByPatientId(patientRequest.getCase_id());
        assertNotNull(recordedPatient.getLatestProvidedTreatment().getTreatment());
    }

    @Test
    public void shouldRecordAllProvidedTreatmentsWhenCreatingPatient() {
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

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
        markForDeletion(allProviders.getAll().toArray());
    }
}
