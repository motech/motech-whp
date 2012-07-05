package org.motechproject.whp.functional.steps.treatmentcard;

import org.joda.time.LocalDate;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.Step;
import org.openqa.selenium.WebDriver;

import static org.motechproject.util.DateUtil.newDate;
import static org.motechproject.whp.functional.data.AdherenceValue.Value;

public class EnterAdherenceOnTreatmentCardStep extends Step {

    private TreatmentCardPage treatmentCardPage;
    private LocalDate weekEndingDate;
    private DayOfWeek dayOfWeek;
    private Value adherenceValue;
    private String phase;

    public EnterAdherenceOnTreatmentCardStep(WebDriver webDriver) {
        super(webDriver);
    }

    public static EnterAdherenceOnTreatmentCardStep onTreatmentCardPage(
            TreatmentCardPage treatmentCardPage,
            WebDriver webDriver) {
        EnterAdherenceOnTreatmentCardStep step = new EnterAdherenceOnTreatmentCardStep(webDriver);
        step.treatmentCardPage = treatmentCardPage;
        return step;
    }

    public EnterAdherenceOnTreatmentCardStep enter(Value adherenceValue) {
        this.adherenceValue = adherenceValue;
        return this;
    }

    public EnterAdherenceOnTreatmentCardStep asAdherenceValueOn(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
        return this;
    }

    public EnterAdherenceOnTreatmentCardStep inSection(String section) {
        this.phase = section;
        return this;
    }

    public EnterAdherenceOnTreatmentCardStep ofWeekEndingOn(int year, int month, int day) {
        LocalDate weekEndingDate = newDate(year, month, day);
        if (DayOfWeek.Sunday.getValue() == weekEndingDate.getDayOfWeek()) {
            this.weekEndingDate = weekEndingDate;
            return this;
        } else {
            throw new RuntimeException("Week ending date should be a sunday");
        }
    }

    @Override
    public TreatmentCardPage execute() {
        treatmentCardPage.setAdherenceValue(adherenceValue, weekEndingDate.withDayOfWeek(dayOfWeek.getValue()), phase);
        return treatmentCardPage.submitAdherence();
    }

}
