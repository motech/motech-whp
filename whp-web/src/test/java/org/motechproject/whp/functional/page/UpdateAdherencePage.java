package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPWebElement;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class UpdateAdherencePage extends Page {

    @FindBy(how = How.ID, using = "weeklyAdherenceForm")
    private WebElement adherenceForm;

    private final String ADHERENCE_WARNING_ID = "adherenceWarning";
    @FindBy(how = How.ID, using = ADHERENCE_WARNING_ID)
    private WebElement adherenceWarning;

    private final String ADHERENCE_CAPTION = "adherenceCaption";
    @FindBy(how = How.ID, using = ADHERENCE_CAPTION)
    private WebElement adherenceCaption;

    @FindBy(how = How.ID, using = "dosesTaken")
    private WebElement numberOfDosesTaken;

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
        numberOfDosesTaken.sendKeys(i + "");
        return this;
    }

    public ProviderPage submit() {
        adherenceForm.submit();
        return MyPageFactory.initElements(webDriver, ProviderPage.class);
    }

    public String getAdherenceWarningText() {
        return adherenceWarning.getText();
    }

    public String getAdherenceCaption() {
        return adherenceCaption.getText();
    }

    public boolean isDosesTaken(int i) {
        return numberOfDosesTaken.getAttribute("value").equals(i + "");
    }
}