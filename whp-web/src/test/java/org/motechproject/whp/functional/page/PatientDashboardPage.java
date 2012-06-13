package org.motechproject.whp.functional.page;

import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PatientDashboardPage extends Page {

    public PatientDashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("IPTreatmentCard");
    }

    public String adherenceStatusOn(LocalDate localDate) {
        return findWebElementByDate(localDate).getAttribute("pillStatus");
    }

    public String adherenceOnProvidedBy(LocalDate localDate) {
        return findWebElementByDate(localDate).getAttribute("providerId");
    }

    public boolean nonEditableAdherenceOn(LocalDate localDate) {
        String appliedCssClasses = findWebElementByDate(localDate).getAttribute("class");
        return !appliedCssClasses.contains("editable");
    }

    public boolean dateNotPresent(LocalDate localDate) {
        try{
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
        return webDriver.findElement(By.id(String.format("%s %s", String.valueOf(localDate.getDayOfMonth()), localDate.toString("MMM YYYY"))));
    }
}
