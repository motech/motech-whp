package org.motechproject.whp.functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.UUID;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public class ListAllPatientsTest extends BaseTest {

    @Autowired
    PatientService patientService;

    @Autowired
    AllPatients allPatients;

    PatientRequest patientRequest;

    @Before
    public void setupPatientForProvider() {
        patientRequest = PatientRequestBuilder.startRecording().withDefaults().withCaseId(UUID.randomUUID().toString()).withProviderId("raj").build();
        patientService.add(patientRequest);
    }

    @Test
    public void shouldLoginAsProviderAndListAllPatientsForProvider() {
        ProviderPage providerPage = loginAsProvider();
        assertTrue(providerPage.hasPatient(patientRequest.getFirstName()));
    }

    ProviderPage loginAsProvider() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword("raj", "password");
    }

    @After
    public void tearDown() throws IOException {
        allPatients.removeAll();
        super.tearDown();
    }
}
