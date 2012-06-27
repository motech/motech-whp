package org.motechproject.whp.functional.page;

import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class TreatmentCardPage extends PatientDashboardPage {

    @FindBy(how = How.ID, using = "setDateLink")
    WebElement adjustStartDatesLink;

    public TreatmentCardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("IPTreatmentCard");
    }

    public String adherenceStatusOn(LocalDate localDate) {
        return findWebElementByDate(localDate).getAttribute("currentPillStatus");
    }

    public String adherenceOnProvidedBy(LocalDate localDate) {
        return findWebElementByDate(localDate).getAttribute("providerId");
    }

    public boolean nonEditableAdherenceOn(LocalDate localDate) {
        String appliedCssClasses = findWebElementByDate(localDate).getAttribute("class");
        return !appliedCssClasses.contains("editable");
    }

    public boolean dateNotPresent(LocalDate localDate) {
        try {
            findWebElementByDate(localDate);
            return false;
        } catch (NoSuchElementException exception) {
            return true;
        }
    }

    public boolean treatmentPausedOn(LocalDate localDate) {
        String appliedCssClasses = findWebElementByDate(localDate).getAttribute("class");
        return appliedCssClasses.contains("pausedAdherenceData");
    }

    private WebElement findWebElementByDate(LocalDate localDate) {
        return webDriver.findElement(By.id(String.format("%s", localDate.toString("d-M-yyyy"))));
    }
}
