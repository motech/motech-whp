package org.motechproject.whp.it.adherence.capture.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.domain.AllDaysOfWeek;
import org.motechproject.whp.common.service.AlertsPropertiesValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AlertsPropertiesValuesIT {

    @Autowired
    AlertsPropertiesValues alertsPropertiesValues;

    @Test
    public void shouldGetAdherenceAlertsWeeks(){
        assertThat(alertsPropertiesValues.getAdherenceMissingWeeks().size(), is(3));
        assertThat(alertsPropertiesValues.getAdherenceMissingWeeks().get(0), is(1));
        assertThat(alertsPropertiesValues.getAdherenceMissingWeeks().get(1), is(2));
        assertThat(alertsPropertiesValues.getAdherenceMissingWeeks().get(2), is(6));
    }

    @Test
    public void shouldGetCumulativeMissedDoses(){
        assertThat(alertsPropertiesValues.getCumulativeMissedDoses().size(), is(1));
        assertThat(alertsPropertiesValues.getCumulativeMissedDoses().get(0), is(10));
    }

    @Test
    public void shouldGetTreatmentNotStartedDays(){
        assertThat(alertsPropertiesValues.getTreatmentNotStartedDays().size(), is(1));
        assertThat(alertsPropertiesValues.getTreatmentNotStartedDays().get(0), is(10));
    }

    @Test
    public void shouldGetDayOfAlertGenerationForCumulativeMissedDoses(){
        assertThat(alertsPropertiesValues.getDayOfAlertGenerationForCumulativeMissedDoses(), is(asList(DayOfWeek.Wednesday)));
    }

    @Test
    public void shouldGetDayOfAlertGenerationForAdherenceMissingWeeks(){
        assertThat(alertsPropertiesValues.getDaysOfAlertGenerationForAdherenceMissingWeeks(), is(asList(DayOfWeek.Wednesday)));
    }

    @Test
    public void shouldGetDayOfAlertGenerationForTreatmentNotStarted(){
        assertThat(alertsPropertiesValues.getDayOfAlertGenerationForTreatmentNotStarted(), is(AllDaysOfWeek.allDaysOfWeek));
    }

    @Test
    public void shouldReadAdherenceMissingWeeksSeverityColors() {
        assertThat(alertsPropertiesValues.getAdherenceMissingSeverityColors(), is(Arrays.asList("", "yellow", "orange", "red")));
    }

    @Test
    public void shouldReadCumulativeMissedDoesSeverityColors() {
        assertThat(alertsPropertiesValues.getCumulativeMissedDosesSeverityColors(), is(Arrays.asList("", "blue")));
    }

    @Test
    public void shouldReadTreatmentNotStartedSeverityColors() {
        assertThat(alertsPropertiesValues.getTreatmentNotStartedSeverityColors(), is(Arrays.asList("", "brown")));
    }

    @Test
    public void shouldReadIPProgressThreshold() {
        assertThat(alertsPropertiesValues.getIPProgressThreshold(), is(asList(100)));
    }

    @Test
    public void shouldReadCPProgressThreshold() {
        assertThat(alertsPropertiesValues.getCPProgressThreshold(), is(asList(100)));
    }

    @Test
    public void shouldGetDayOfAlertGenerationForIPProgress(){
        assertThat(alertsPropertiesValues.getDayOfAlertGenerationForIPProgress(), is(AllDaysOfWeek.allDaysOfWeek));
    }

    @Test
    public void shouldGetDayOfAlertGenerationForCPProgress(){
        assertThat(alertsPropertiesValues.getDayOfAlertGenerationForCPProgress(), is(AllDaysOfWeek.allDaysOfWeek));
    }
}
