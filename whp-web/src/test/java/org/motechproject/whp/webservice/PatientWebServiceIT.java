package org.motechproject.whp.webservice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.application.service.PatientRegistrationService;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.exception.WHPValidationException;
import org.motechproject.whp.domain.Patient;
import org.motechproject.whp.domain.ProvidedTreatment;
import org.motechproject.whp.repository.AllPatients;
import org.motechproject.whp.repository.AllTreatments;
import org.motechproject.whp.repository.SpringIntegrationTest;
import org.motechproject.whp.request.PatientRequest;
import org.motechproject.whp.validation.validator.BeanValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:META-INF/spring/applicationContext.xml")
public class PatientWebServiceIT extends SpringIntegrationTest {

    @Autowired
    PatientRegistrationService patientRegistrationService;
    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;
    @Autowired
    private BeanValidator validator;

    private PatientWebService patientWebService;

    @Before
    public void setUp() {
        initMocks(this);
        patientWebService = new PatientWebService(patientRegistrationService, allTreatments, validator);
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
    }

    @Test
    public void shouldCreatePatient() throws WHPValidationException {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();

        patientWebService.createCase(patientRequest);

        Patient patient = allPatients.findByPatientId(patientRequest.getCase_id());

        assertNotNull(patient);
        assertNotNull(patient.getLatestProvidedTreatment().getTreatment());
        for (ProvidedTreatment providedTreatment : patient.getProvidedTreatments()) {
            assertNotNull(providedTreatment.getTreatment());
        }
    }

}
