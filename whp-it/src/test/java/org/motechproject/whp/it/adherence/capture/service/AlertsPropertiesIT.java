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
public class AlertsPropertiesIT {

    @Autowired
    AlertsPropertiesValues alertsPropertiesValues;

    @Test
    public void shouldGetAdherenceAlertsWeeks(){
        assertThat(alertsPropertiesValues.getAdherenceMissingWeeks().size(), is(3));
    }
}
