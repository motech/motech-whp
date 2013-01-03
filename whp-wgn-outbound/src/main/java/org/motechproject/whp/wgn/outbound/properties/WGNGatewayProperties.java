package org.motechproject.whp.wgn.outbound.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class WGNGatewayProperties {

    @Value("#{wgnGatewayProperty['api.key.name']}")
    private String apiKeyName;

    @Value("#{wgnGatewayProperty['api.key.value']}")
    private String apiKeyValue;

    public String getApiKeyValue() {
        return apiKeyValue;
    }

    public String getApiKeyName() {
        return apiKeyName;
    }
}
