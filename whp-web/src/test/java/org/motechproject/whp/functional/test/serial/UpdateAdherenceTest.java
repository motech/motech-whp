package org.motechproject.whp.functional.test.serial;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.ProviderPage;
import org.motechproject.whp.functional.page.UpdateAdherencePage;
import org.motechproject.whp.functional.service.ProviderDataService;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.contract.PatientRequest;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationFunctionalTestContext.xml")
public class UpdateAdherenceTest extends BaseTest {

    @Autowired
    PatientService patientService;

    ProviderDataService providerDataService;

    PatientRequest patientRequest;
    TestProvider provider;

    @Override
    public void setUp() {
        super.setUp();
        setupProvider();
        setupPatientForProvider();
    }

    public void setupProvider() {
        providerDataService = new ProviderDataService(webDriver);
        provider = providerDataService.createProvider();
    }

    public void setupPatientForProvider() {
        TreatmentCategory oldCategory = new TreatmentCategory("RNTCP Category 1", "01", 3, 8, 18, Arrays.asList(DayOfWeek.Monday, DayOfWeek.Wednesday, DayOfWeek.Friday));
        patientRequest = new PatientRequestBuilder()
                .withDefaults()
                .withLastModifiedDate(DateUtil.newDateTime(1990, 3, 17, 4, 55, 50))
                .withTreatmentCategory(oldCategory)
                .withCaseId(UUID.randomUUID().toString())
                .withProviderId(provider.getProviderId())
                .build();
        patientService.createPatient(patientRequest);
    }

    @Test
    public void shouldAllowUpdateAdherenceOnSunday() {
        adjustDateTime(DateTime.now().withDayOfWeek(7));

        UpdateAdherencePage updateAdherencePage = loginAsProvider().clickEditAdherenceLink(patientRequest.getCase_id()).markAsTaken("Monday");
        boolean isTaken = updateAdherencePage.submit().clickEditAdherenceLink(patientRequest.getCase_id()).isTaken("Monday");
        assertTrue(isTaken);
    }

    @Test
    public void shouldNotAllowUpdateAdherenceOnWednesday() {
        adjustDateTime(DateTime.now().withDayOfWeek(3));

        boolean isReadOnly = loginAsProvider().clickEditAdherenceLink(patientRequest.getCase_id()).isReadOnly();
        assertTrue(isReadOnly);
    }

    ProviderPage loginAsProvider() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }

    @After
    public void tearDown() throws IOException {
        super.tearDown();
    }
}
