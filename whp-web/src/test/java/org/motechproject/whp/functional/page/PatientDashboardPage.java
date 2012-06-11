package org.motechproject.whp.functional.page;

import org.joda.time.LocalDate;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class PatientDashboardPage extends Page {

    public PatientDashboardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("IPTreatmentCard");
    }

    public String adherenceStatusOn(LocalDate localDate) {
        return webDriver.findElement(By.id(String.format("%s %s", String.valueOf(localDate.getDayOfMonth()), localDate.toString("MMM YYYY")))).getAttribute("pillStatus");
    }

    public String adherenceOnProvidedBy(LocalDate localDate) {
        return webDriver.findElement(By.id(String.format("%s %s", String.valueOf(localDate.getDayOfMonth()), localDate.toString("MMM YYYY")))).getAttribute("providerId");
    }

    public boolean nonEditableAdherenceOn(LocalDate localDate) {
        String appliedCssClasses = webDriver.findElement(By.id(String.format("%s %s", String.valueOf(localDate.getDayOfMonth()), localDate.toString("MMM YYYY")))).getAttribute("class");
        return !appliedCssClasses.contains("editable");
    }

    public boolean dateNotPresent(LocalDate localDate) {
        try{
            webDriver.findElement(By.id(String.format("%s %s", String.valueOf(localDate.getDayOfMonth()), localDate.toString("MMM YYYY"))));
            return false;
        } catch (NoSuchElementException exception) {
            return true;
        }
    }
}
