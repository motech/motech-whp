package org.motechproject.whp.uimodel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.alerts.ColorConfiguration;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PatientDashboardLegendsTest {

    PatientDashboardLegends patientDashboardLegends;
    @Mock
    ColorConfiguration colorConfiguration;

    @Before
    public void setUp() {
        initMocks(this);
        setUpDefaultColorConfiguration();

        patientDashboardLegends = new PatientDashboardLegends(colorConfiguration);
    }

    private void setUpDefaultColorConfiguration() {
        when(colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 1)).thenReturn("yellow");
        when(colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 2)).thenReturn("red");
        when(colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 3)).thenReturn("maroon");
        when(colorConfiguration.getColorFor(PatientAlertType.CumulativeMissedDoses, 1)).thenReturn("blue");
        when(colorConfiguration.getColorFor(PatientAlertType.TreatmentNotStarted, 1)).thenReturn("brown");
    }

    @Test
    public void shouldReturnColorAndDisplayTextForEachAlertAndSeverity() {

        List<Legend> legends = patientDashboardLegends.getLegends();

        assertEquals(new Legend("pink", "message.current.treatment.paused"), legends.get(0));
        assertEquals(new Legend("yellow", "message.alert.filter.adherence.missing.severity.one.alerts"), legends.get(1));
        assertEquals(new Legend("red", "message.alert.filter.adherence.missing.severity.two.alerts"), legends.get(2));
        assertEquals(new Legend("maroon", "message.alert.filter.adherence.missing.severity.three.alerts"), legends.get(3));
        assertEquals(new Legend("blue", "message.alert.filter.cumulative.missed.dose.alerts"), legends.get(4));
        assertEquals(new Legend("brown", "message.alert.filter.treatment.not.started.alerts"), legends.get(5));
    }

}
