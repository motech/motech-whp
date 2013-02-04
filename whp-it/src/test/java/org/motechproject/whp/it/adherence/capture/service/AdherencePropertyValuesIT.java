package org.motechproject.whp.it.adherence.capture.service;

import org.junit.Test;
import org.motechproject.whp.common.service.AdherencePropertyValues;
import org.motechproject.whp.it.SpringIntegrationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@ContextConfiguration(locations = "classpath*:/applicationITContext.xml")
public class AdherencePropertyValuesIT extends SpringIntegrationTest {

    @Autowired
    AdherencePropertyValues adherenceProperties;

    @Test
    public void shouldLoadAdherenceProperties() {
        assertThat(adherenceProperties.validAdherenceDays().size(), greaterThan(0));
    }

}
