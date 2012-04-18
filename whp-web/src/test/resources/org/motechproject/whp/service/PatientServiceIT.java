package org.motechproject.whp.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.builder.PatientRequestBuilder;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.ProvidedTreatment;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.repository.AllTreatments;
import org.motechproject.whp.request.PatientRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration(locations = "classpath*:/applicationContext.xml")
public class PatientServiceIT extends SpringIntegrationTest {

    @Autowired
    AllPatients allPatients;
    @Autowired
    AllTreatments allTreatments;

    PatientService patientService;

    @Before
    public void setUp() {
        initMocks(this);
        patientService = new PatientService(allPatients, allTreatments);
    }

    @After
    public void tearDown() {
        markForDeletion(allPatients.getAll().toArray());
        markForDeletion(allTreatments.getAll().toArray());
    }

    @Test
    public void shouldCreatePatient() {
        PatientRequest patientRequest = new PatientRequestBuilder().withDefaults().build();

        patientService.createCase(patientRequest);

        Patient patient = allPatients.findByPatientId("caseId");

        assertNotNull(patient);
        assertNotNull(patient.getLatestProvidedTreatment().getTreatment());
        for (ProvidedTreatment providedTreatment : patient.getProvidedTreatments()) {
            assertNotNull(providedTreatment.getTreatment());
        }
    }

}
