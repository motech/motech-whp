package org.motechproject.whp.wgn.outbound.service;

import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.wgn.outbound.properties.WGNGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;

@Service
public class WGNGateway {

    private HttpClientService httpClientService;
    private WGNGatewayProperties wgnGatewayProperties;

    @Autowired
    public WGNGateway(HttpClientService httpClientService, WGNGatewayProperties wgnGatewayProperties) {
        this.httpClientService = httpClientService;
        this.wgnGatewayProperties = wgnGatewayProperties;
    }

    public void post(String url, Serializable request) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(wgnGatewayProperties.getApiKeyName(), wgnGatewayProperties.getApiKeyValue());
        httpClientService.post(url, request, headers);
    }
}
