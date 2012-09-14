package org.motechproject.whp.container.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SputumTrackingPropertiesTest {
    @Mock
    private Properties properties;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldMapDNPStatusCodeToCampaignMessageStatus() {
        when(properties.getProperty("containerIdMaxLength")).thenReturn("11");
        SputumTrackingProperties sputumTrackingProperties = new SputumTrackingProperties(properties);
        assertEquals(11, sputumTrackingProperties.getContainerIdMaxLength());
    }
}
