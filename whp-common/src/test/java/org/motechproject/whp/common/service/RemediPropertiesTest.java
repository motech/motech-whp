package org.motechproject.whp.common.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class RemediPropertiesTest {

    @Mock
    private Properties remediProperty;
    private RemediProperties remediProperties;

    @Before
    public void setUp() {
        initMocks(this);
        remediProperties = new RemediProperties(remediProperty);
    }

    @Test
    public void shouldGetRemediUrlFromPropertyFile() {
        when(remediProperty.getProperty("remedi.url")).thenReturn("myUrl");
        String url = remediProperties.getUrl();
        assertEquals("myUrl", url);
    }

    @Test
    public void shouldGetRemediApiKeyFromPropertyFile() {
        when(remediProperty.getProperty("remedi.api.key")).thenReturn("myApiKey");
        String apiKey = remediProperties.getApiKey();
        assertEquals("myApiKey", apiKey);
    }
}
