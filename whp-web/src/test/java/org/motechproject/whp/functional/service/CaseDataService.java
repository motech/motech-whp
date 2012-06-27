package org.motechproject.whp.functional.service;

import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.page.CaseUpdatePage;
import org.motechproject.whp.functional.page.PatientCreatePage;
import org.openqa.selenium.WebDriver;

import java.util.UUID;

public class CaseDataService {

    private WebDriver webDriver;

    public CaseDataService(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public void updateCase(String requestData) {
        CaseUpdatePage caseUpdatePage = CaseUpdatePage.fetch(webDriver);
        caseUpdatePage.updateCase(requestData);
    }
}
