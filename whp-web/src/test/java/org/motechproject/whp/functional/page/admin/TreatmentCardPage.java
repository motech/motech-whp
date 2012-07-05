package org.motechproject.whp.functional.page.admin;

import org.joda.time.LocalDate;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.refdata.domain.PhaseName;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import static junit.framework.Assert.fail;
import static org.apache.commons.lang.StringUtils.trimToEmpty;
import static org.motechproject.whp.functional.data.AdherenceValue.*;
import static org.openqa.selenium.By.id;

public class TreatmentCardPage extends PatientDashboardPage {

    @FindBy(how = How.ID, using = "setDateLink")
    WebElement adjustStartDatesLink;

    @FindBy(how = How.ID, using = "patientCurrentPhase")
    WebElement patientPhaseMessage;

    @FindBy(how = How.ID, using = "submitAdherence")
    WebElement submit;


    public TreatmentCardPage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    public void postInitialize() {
        super.postInitialize();
        adjustStartDatesLink = WebDriverFactory.createWebElement(adjustStartDatesLink);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("IPTreatmentCard");
    }

    public String adherenceStatusOn(LocalDate localDate, String section) {
        return findCell(localDate, section).getAttribute("currentPillStatus");
    }

    public String adherenceOnProvidedBy(LocalDate localDate, String section) {
        return findCell(localDate, section).getAttribute("providerId");
    }

    public boolean nonEditableAdherenceOn(LocalDate localDate, String section) {
        String appliedCssClasses = findCell(localDate, section).getAttribute("class");
        return !appliedCssClasses.contains("editable");
    }

    public boolean dateNotPresent(LocalDate localDate, String section) {
        try {
            findCell(localDate, section);
            return false;
        } catch (NoSuchElementException exception) {
            return true;
        }
    }

    public boolean treatmentPausedOn(LocalDate localDate, String section) {
        String appliedCssClasses = findCell(localDate, section).getAttribute("class");
        return appliedCssClasses.contains("pausedAdherenceData");
    }

    public void transitionPatient(PhaseName phaseName) {
        try {
            WebElement transitionToEIPLink = webDriver.findElement(By.id(phaseName.name()));
            transitionToEIPLink.click();
        } catch (NoSuchElementException exception) {
            fail();
        }
    }

    public boolean currentPhase(PhaseName phaseName) {
        String patientCurrentPhase = webDriver.findElement(By.id("patientCurrentPhase")).getText();
        return patientCurrentPhase.contains(phaseName.name());
    }

    public String getCurrentPhaseOfPatient() {
        return trimToEmpty(patientPhaseMessage.getText().split(":")[1]);
    }

    public void setAdherenceValue(Value adherenceValue, LocalDate on, String phase) {
        WebElement cell = findCell(on, phase);
        Integer distanceInNumberOfClicks = getDistanceInNumberOfClicks(getCurrentValue(cell), adherenceValue);
        for (int i = 0; i < distanceInNumberOfClicks; i++) {
            cell.click();
        }
    }

    public TreatmentCardPage submitAdherence() {
        submit.click();
        waitForElementToBeReloadedByAjax();
        return MyPageFactory.initElements(webDriver, TreatmentCardPage.class);
    }

    private WebElement findCell(LocalDate localDate, String section) {
        Integer day = localDate.getDayOfMonth();
        Integer month = localDate.getMonthOfYear();
        Integer year = localDate.getYear();

        String id = String.format("%s-%s-%s-%s", section, day, month, year);
        return webDriver.findElement(id(id));
    }

}
