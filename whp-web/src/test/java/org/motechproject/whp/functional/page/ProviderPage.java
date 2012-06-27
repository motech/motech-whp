package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class ProviderPage extends ListPatientsPage {

    @FindBy(how = How.CLASS_NAME, using = "updateAdherenceLink")
    private List<WebElement> updateAdherenceLinks;

    public ProviderPage(WebDriver webDriver) {
        super(webDriver);
    }

    public UpdateAdherencePage clickEditAdherenceLink(String patientId) {
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
}
