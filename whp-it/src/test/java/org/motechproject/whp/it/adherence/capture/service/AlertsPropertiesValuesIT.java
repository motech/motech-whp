package org.motechproject.whp.it.adherence.capture.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.common.service.AlertsPropertiesValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
        assertThat(alertsPropertiesValues.getCumulativeMissedDoses(), is(10));
    }

    @Test
    public void shouldGetTreatmentNotStartedDays(){
        assertThat(alertsPropertiesValues.getTreatmentNotStartedDays(), is(10));
    }
}
