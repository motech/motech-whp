package org.motechproject.whp.it.wgn.outbound;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.whp.wgn.outbound.properties.WGNGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/applicationWgnOutboundContext.xml")
public class WgnOutboundPropertiesIT {

    @Autowired
    private WGNGatewayProperties gatewayProperties;

    @Test
    public void shouldReturnConfigurationForWgnOutbound() {
        assertEquals("API_KEY", gatewayProperties.getApiKeyName());
        assertEquals("123456", gatewayProperties.getApiKeyValue());
    }
}
