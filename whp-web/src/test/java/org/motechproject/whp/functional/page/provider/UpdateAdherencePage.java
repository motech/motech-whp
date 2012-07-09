package org.motechproject.whp.functional.page.provider;

import org.motechproject.whp.functional.framework.WHPWebElement;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

public class UpdateAdherencePage extends Page {

    @FindBy(how = How.ID, using = "weeklyAdherenceForm")
    private WebElement adherenceForm;

    @FindBy(how = How.ID, using = "submit")
    private WebElement submit;

    private final String ADHERENCE_WARNING_ID = "adherenceWarning";
    @FindBy(how = How.ID, using = ADHERENCE_WARNING_ID)
    private WebElement adherenceWarning;

    private final String ADHERENCE_CAPTION = "adherenceCaption";
    @FindBy(how = How.ID, using = ADHERENCE_CAPTION)
    private WebElement adherenceCaption;

    @FindBy(how = How.ID, using = "dosesTaken")
    private WebElement numberOfDosesTaken;

    @Override
    public void postInitialize() {
        adherenceForm = WebDriverFactory.createWebElement(adherenceForm);
        submit = WebDriverFactory.createWebElement(submit);
        adherenceWarning = WebDriverFactory.createWebElement(adherenceWarning);
        adherenceCaption = WebDriverFactory.createWebElement(adherenceCaption);
        numberOfDosesTaken = WebDriverFactory.createWebElement(numberOfDosesTaken);
    }

    public UpdateAdherencePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("weeklyAdherenceForm");
    }

    public boolean isReadOnly() {
        return !numberOfDosesTaken.isEnabled();
    }

    public UpdateAdherencePage setNumberOfDosesTaken(int i) {
        ((WHPWebElement) numberOfDosesTaken).select(i + "");
        return this;
    }

    public ProviderPage submit() {
        submit.click();
        return getProviderPage(webDriver);
    }

    public String getAdherenceWarningText() {
        return adherenceWarning.getText();
    }

    public String getAdherenceCaption() {
        return adherenceCaption.getText();
    }

    public boolean isDosesTaken(int i) {
        List<WebElement> doseOptions = numberOfDosesTaken.findElements(By.tagName("option"));
        for (WebElement doseOption : doseOptions) {
            if (doseOption.getAttribute("value").equals(i + "")) return true;
        }
        return false;
    }
}