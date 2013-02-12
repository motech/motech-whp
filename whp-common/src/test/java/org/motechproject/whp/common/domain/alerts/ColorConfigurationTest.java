package org.motechproject.whp.common.domain.alerts;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.service.AlertsPropertiesValues;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class ColorConfigurationTest {
    @Mock
    private AlertsPropertiesValues alertsPropertiesValues;
    private ColorConfiguration colorConfiguration;

    @Before
    public void setUp() {
        initMocks(this);
        colorConfiguration = new ColorConfiguration(alertsPropertiesValues);
    }

    @Test
    public void shouldGetAlertSeverityColorsForAdherenceMissingAlertType() {
        when(alertsPropertiesValues.getAdherenceMissingSeverityColors()).thenReturn(asList("no_color", "c1", "c2", "c3"));

        assertEquals("c2", colorConfiguration.getColorFor(PatientAlertType.AdherenceMissing, 2));
    }

    @Test
    public void shouldGetAlertSeverityColorsForCumulativeMissedDosesAlertType() {
        when(alertsPropertiesValues.getCumulativeMissedDosesSeverityColors()).thenReturn(asList("no_color", "c1", "c2", "c3"));

        assertEquals("c2", colorConfiguration.getColorFor(PatientAlertType.CumulativeMissedDoses, 2));
    }

    @Test
    public void shouldGetAlertSeverityColorsForTreatmentNotStartedAlertType() {
        when(alertsPropertiesValues.getTreatmentNotStartedSeverityColors()).thenReturn(asList("no_color", "c1", "c2", "c3"));

        assertEquals("c2", colorConfiguration.getColorFor(PatientAlertType.TreatmentNotStarted, 2));
    }

    @Test
    public void shouldGetDefaultAlertSeverityColorForAnEdgeCaseSeverity() {
        when(alertsPropertiesValues.getTreatmentNotStartedSeverityColors()).thenReturn(asList("no_color", "c1", "c2", "c3"));

        assertEquals(ColorConfiguration.DEFAULT_COLOR, colorConfiguration.getColorFor(PatientAlertType.TreatmentNotStarted, 4));
    }

    @Test
    public void shouldGetDefaultAlertSeverityColorForInvalidSeverity() {
        when(alertsPropertiesValues.getTreatmentNotStartedSeverityColors()).thenReturn(asList("no_color", "c1", "c2", "c3"));

        assertEquals(ColorConfiguration.DEFAULT_COLOR, colorConfiguration.getColorFor(PatientAlertType.TreatmentNotStarted, 6));
    }
}
