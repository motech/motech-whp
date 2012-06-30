package org.motechproject.whp.functional.service;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.remedi.PatientCreatePage;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class PatientDataService {

    private WebDriver webDriver;

    public PatientDataService(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public TestPatient createPatient(String providerId, String firstName, String district) {
        TestPatient testPatient = new TestPatient(UUID.randomUUID().toString(), PatientBuilder.TB_ID, providerId, firstName, district);
        PatientCreatePage patientCreatePage = PatientCreatePage.fetch(webDriver);
        patientCreatePage.createPatient(testPatient);
        return testPatient;
    }
}
