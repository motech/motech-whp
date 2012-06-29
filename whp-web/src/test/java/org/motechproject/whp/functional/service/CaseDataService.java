package org.motechproject.whp.functional.service;

import org.motechproject.whp.functional.page.remedi.CaseUpdatePage;
import org.openqa.selenium.WebDriver;

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
