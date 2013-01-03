package org.motechproject.whp.wgn.outbound.service;

import org.apache.log4j.Logger;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.wgn.outbound.WGNRequest;
import org.motechproject.whp.wgn.outbound.logging.WGNRequestLogger;
import org.motechproject.whp.wgn.outbound.properties.WGNGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static org.apache.log4j.Logger.getLogger;

@Service
public class WGNGateway {

    private HttpClientService httpClientService;
    private WGNGatewayProperties wgnGatewayProperties;
    private Logger logger;

    @Autowired
    public WGNGateway(HttpClientService httpClientService, WGNGatewayProperties wgnGatewayProperties) {
        this.httpClientService = httpClientService;
        this.wgnGatewayProperties = wgnGatewayProperties;
        this.logger = getLogger(WGNGateway.class);
    }

    public void post(String url, WGNRequest request) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(wgnGatewayProperties.getApiKeyName(), wgnGatewayProperties.getApiKeyValue());
        httpClientService.post(url, request.toXML(), headers);
        log(url, request);
    }

    private void log(String url, WGNRequest request) {
        new WGNRequestLogger(logger).log(url, request);
    }
}
