package org.motechproject.whp.functional.page.provider;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ProviderPage extends ListPatientsPage {

    private List<WebElement> updateAdherenceLinks;

    public ProviderPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("patientList");
    }

    public UpdateAdherencePage clickEditAdherenceLink(String patientId) {
        waitForElementWithCSSToLoad("patientId");
        patientIds = webDriver.findElements(By.className("patientId"));
        waitForElementWithCSSToLoad("updateAdherenceLink");
        updateAdherenceLinks = webDriver.findElements(By.className("updateAdherenceLink"));

        int index = -1;
        for (int i = 0; i < patientIds.size(); i++) {
            if (patientIds.get(i).getText().equals(patientId)) {
                index = i;
                break;
            }
        }
        updateAdherenceLinks.get(index).findElement(By.linkText("Edit")).click();
        UpdateAdherencePage updateAdherencePage = MyPageFactory.initElements(webDriver, UpdateAdherencePage.class);
        assertTrue(updateAdherencePage.getAdherenceCaption().contains(patientId));
        return updateAdherencePage;

    }

    public boolean adherenceLoggedForCurrentWeek(String caseId) {
        waitForElementWithCSSToLoad("adherenceCaptured");
        WebElement adherenceLoggedForPatient = webDriver.findElement(By.id(String.format("patient_%s_AdherenceTaken", caseId)));
        return adherenceLoggedForPatient.isDisplayed() && adherenceLoggedForPatient.getText().contains("YES") && adherenceLoggedForPatient.getAttribute("class").contains("adherenceCaptured");
    }
}
