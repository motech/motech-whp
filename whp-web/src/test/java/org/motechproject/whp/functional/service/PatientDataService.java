package org.motechproject.whp.functional.service;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.PatientCreatePage;
import org.motechproject.whp.functional.page.ProviderActivatePage;
import org.motechproject.whp.functional.page.ProviderCreatePage;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class PatientDataService {

    private WebDriver webDriver;

    public PatientDataService(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public TestPatient createPatient(String providerId, String firstName) {
        TestPatient testPatient = new TestPatient(UUID.randomUUID().toString(), "12345678891", providerId, firstName);
        PatientCreatePage patientCreatePage = PatientCreatePage.fetch(webDriver);
        patientCreatePage.createPatient(testPatient);
        return testPatient;
    }
}
