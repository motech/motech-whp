package org.motechproject.whp.common.domain.alerts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.AllDaysOfWeek;
import org.motechproject.whp.common.service.AlertsPropertiesValues;

import java.util.Map;

import static java.util.Arrays.asList;
import static junit.framework.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.*;

public class AllAlertConfigurationsTest {

    @Mock
    AlertsPropertiesValues alertsPropertiesValues;

    private AllAlertConfigurations allAlertConfigurations;

    @Before
    public void setUp() {
        initMocks(this);
        when(alertsPropertiesValues.getAdherenceMissingSeverityColors()).thenReturn(asList("", "yellow", "pink", "red"));
        when(alertsPropertiesValues.getCumulativeMissedDosesSeverityColors()).thenReturn(asList("", "blue"));
        when(alertsPropertiesValues.getTreatmentNotStartedSeverityColors()).thenReturn(asList("", "brown"));
    }

    @Test
    public void shouldReturnAlertThresholdSeverity() {
        when(alertsPropertiesValues.getAdherenceMissingWeeks()).thenReturn(asList(1, 2, 6));
         allAlertConfigurations = new AllAlertConfigurations(alertsPropertiesValues);

        assertEquals((Integer) 1, allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 1));
        assertEquals((Integer) 2, allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 2));
        assertEquals((Integer) 3, allAlertConfigurations.getAlertSeverityFor(AdherenceMissing, 6));
    }

    @Test
    public void shouldReturnIfAlertShouldBeRunToday() {
        when(alertsPropertiesValues.getDaysOfAlertGenerationForAdherenceMissingWeeks()).thenReturn(asList(DayOfWeek.getDayOfWeek(DateUtil.today())));
        when(alertsPropertiesValues.getDayOfAlertGenerationForCumulativeMissedDoses()).thenReturn(asList(DayOfWeek.getDayOfWeek(DateUtil.today().plusDays(1))));
        when(alertsPropertiesValues.getDayOfAlertGenerationForTreatmentNotStarted()).thenReturn(asList(DayOfWeek.getDayOfWeek(DateUtil.today())));
        allAlertConfigurations = new AllAlertConfigurations(alertsPropertiesValues);

        assertTrue(allAlertConfigurations.shouldRunToday(AdherenceMissing));
        assertFalse(allAlertConfigurations.shouldRunToday(CumulativeMissedDoses));
        assertTrue(allAlertConfigurations.shouldRunToday(TreatmentNotStarted));
    }

    @Test
    public void shouldReturnAlertConfigurations() {
        when(alertsPropertiesValues.getAdherenceMissingWeeks()).thenReturn(asList(1, 2, 6));
        when(alertsPropertiesValues.getCumulativeMissedDoses()).thenReturn(asList(10));
        when(alertsPropertiesValues.getTreatmentNotStartedDays()).thenReturn(asList(10));
        when(alertsPropertiesValues.getDaysOfAlertGenerationForAdherenceMissingWeeks()).thenReturn(asList(DayOfWeek.Wednesday));
        when(alertsPropertiesValues.getDayOfAlertGenerationForCumulativeMissedDoses()).thenReturn(asList(DayOfWeek.Wednesday));
        when(alertsPropertiesValues.getDayOfAlertGenerationForTreatmentNotStarted()).thenReturn(AllDaysOfWeek.allDaysOfWeek);
        when(alertsPropertiesValues.getDayOfAlertGenerationForIPProgress()).thenReturn(AllDaysOfWeek.allDaysOfWeek);
        when(alertsPropertiesValues.getDayOfAlertGenerationForCPProgress()).thenReturn(AllDaysOfWeek.allDaysOfWeek);
        when(alertsPropertiesValues.getIPProgressThreshold()).thenReturn(asList(110));
        when(alertsPropertiesValues.getCPProgressThreshold()).thenReturn(asList(120));

        AllAlertConfigurations configurations = new AllAlertConfigurations(alertsPropertiesValues);

        Map<PatientAlertType, AlertConfiguration> alertConfigurationMap = configurations.alertConfigurationMap;
        assertEquals((Integer) 1, alertConfigurationMap.get(AdherenceMissing).getAlertThresholds().getThreshold(1).getThreshold());
        assertEquals((Integer) 10, alertConfigurationMap.get(CumulativeMissedDoses).getAlertThresholds().getThreshold(10).getThreshold());
        assertEquals((Integer) 10, alertConfigurationMap.get(TreatmentNotStarted).getAlertThresholds().getThreshold(10).getThreshold());
        assertEquals((Integer) 110, alertConfigurationMap.get(IPProgress).getAlertThresholds().getThreshold(110).getThreshold());
        assertEquals((Integer) 120, alertConfigurationMap.get(CPProgress).getAlertThresholds().getThreshold(120).getThreshold());

        assertEquals(asList(DayOfWeek.Wednesday), alertConfigurationMap.get(AdherenceMissing).getDaysOfWeek());
        assertEquals(asList(DayOfWeek.Wednesday), alertConfigurationMap.get(CumulativeMissedDoses).getDaysOfWeek());
        assertEquals(AllDaysOfWeek.allDaysOfWeek, alertConfigurationMap.get(TreatmentNotStarted).getDaysOfWeek());
        assertEquals(AllDaysOfWeek.allDaysOfWeek, alertConfigurationMap.get(IPProgress).getDaysOfWeek());
        assertEquals(AllDaysOfWeek.allDaysOfWeek, alertConfigurationMap.get(CPProgress).getDaysOfWeek());

        assertThat(configurations.getAlertSeverityFor(IPProgress, 110), is(1));
        assertThat(configurations.getAlertSeverityFor(CPProgress, 120), is(1));

    }

}
