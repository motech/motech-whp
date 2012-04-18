package org.motechproject.whp.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.integration.repository.SpringIntegrationTest;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.PatientType;
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
        PatientRequest patientRequest = new PatientRequest()
                .setPatientInfo("caseId", "Foo", "Bar", "M", PatientType.PHSTransfer.name(), "12345667890")
                .setRegistrationDetails("regNum", DateUtil.today().toString())
                .setSmearTestResults("Pre-treatment1", DateUtil.today().minusDays(10).toString(), "result1", "Pre-treatment2", DateUtil.today().minusDays(5).toString(), "result2")
                .setTreatmentData("01", "providerId01seq1", "providerId")
                .setPatientAddress("house number", "landmark", "block", "village", "district", "state");

        patientService.createCase(patientRequest);

        Patient patient = allPatients.findByPatientId("caseId");

        assertNotNull(patient);
        assertNotNull(patient.getLatestProvidedTreatment().getTreatment());
        for (ProvidedTreatment providedTreatment : patient.getProvidedTreatments()) {
            assertNotNull(providedTreatment.getTreatment());
        }
    }

}
