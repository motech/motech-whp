package org.motechproject.whp.it.common.alerts;

import org.junit.Test;
import org.motechproject.whp.common.domain.alerts.AllAlertConfigurations;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.service.AlertsPropertiesValues;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AllAlertConfigurationsIT extends SpringIntegrationTest {

    AllAlertConfigurations allAlertConfigurations;

    @Autowired
    AlertsPropertiesValues alertsPropertiesValues;

    @Test
    public void shouldReadPropertiesToGetAlertSeverity() {
        allAlertConfigurations = new AllAlertConfigurations(alertsPropertiesValues);
        assertThat(allAlertConfigurations.getAlertSeverityFor(PatientAlertType.AdherenceMissing, 4), is(2));
    }

    @Test
    public void shouldReadCumulativeMissedDoesValues() {
        allAlertConfigurations = new AllAlertConfigurations(alertsPropertiesValues);
        assertThat(allAlertConfigurations.getAlertSeverityFor(PatientAlertType.CumulativeMissedDoses, 10), is(1));
    }

    @Test
    public void shouldReadTreatmentNotStartedDays() {
        allAlertConfigurations = new AllAlertConfigurations(alertsPropertiesValues);
        assertThat(allAlertConfigurations.getAlertSeverityFor(PatientAlertType.TreatmentNotStarted, 10), is(1));
    }
}
