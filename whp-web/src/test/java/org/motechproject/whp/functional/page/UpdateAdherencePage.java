package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class UpdateAdherencePage extends Page {

    @FindBy(how = How.ID, using = "weeklyAdherenceForm")
    private WebElement adherenceForm;

    private final String ADHERENCE_WARNING_ID = "adherenceWarning";
    @FindBy(how = How.ID, using = ADHERENCE_WARNING_ID)
    private WebElement adherenceWarning;

    @FindBy(how = How.CLASS_NAME, using = "adherenceRow")
    private List<WebElement> adherenceRows;

    public UpdateAdherencePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("weeklyAdherenceForm");
    }

    public boolean isTaken(String dayOfWeek) {
        for (WebElement adherenceRow : adherenceRows) {
            String pillDay = adherenceRow.findElement(By.className("pillDay")).getAttribute("value");
            if (pillDay.equals(dayOfWeek)) {
                return adherenceRow.findElement(By.className("pillStatusTaken")).isSelected();
            }
        }
        return false;
    }

    public UpdateAdherencePage markAsTaken(String dayOfWeek) {
        for (WebElement adherenceRow : adherenceRows) {
            String pillDay = adherenceRow.findElement(By.className("pillDay")).getAttribute("value");
            if (pillDay.equals(dayOfWeek)) {
                adherenceRow.findElement(By.className("pillStatusTaken")).click();
                break;
            }
        }
        return this;
    }

    public boolean isReadOnly() {
        return !adherenceRows.get(0).findElement(By.className("pillStatusTaken")).isEnabled();
    }

    public String getAdherenceWarningText() {
        return adherenceWarning.getText();
    }

    public ProviderPage submit() {
        adherenceForm.submit();
        return MyPageFactory.initElements(webDriver, ProviderPage.class);
    }

}
