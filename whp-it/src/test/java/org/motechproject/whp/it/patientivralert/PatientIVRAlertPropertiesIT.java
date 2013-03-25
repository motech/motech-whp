package org.motechproject.whp.it.patientivralert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.patientivralert.configuration.PatientIVRAlertProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class PatientIVRAlertPropertiesIT {

    @Autowired
    private PatientIVRAlertProperties patientIVRAlertProperties;

    @Test
    public void shouldReturnBatchConfigurationForProviderReminderScheduler() {
        assertEquals(30, patientIVRAlertProperties.getBatchSize());
        assertNotNull(patientIVRAlertProperties.getPatientIVRRequestURL());
    }
}
