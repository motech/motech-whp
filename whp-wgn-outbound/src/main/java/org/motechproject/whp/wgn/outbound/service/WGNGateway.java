package org.motechproject.whp.wgn.outbound.service;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.http.client.domain.EventCallBack;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.wgn.outbound.WGNRequest;
import org.motechproject.whp.wgn.outbound.properties.WGNGatewayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Serializable;
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
        String requestXML = request.toXML();
        httpClientService.post(url, requestXML, headers);
        log(url, requestXML);
    }

    public void post(String url, Serializable request, EventCallBack eventCallBack) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(wgnGatewayProperties.getApiKeyName(), wgnGatewayProperties.getApiKeyValue());
        httpClientService.post(url, request, headers, eventCallBack);
        log(url, toJson(request));
    }

    private String toJson(Serializable request) {
        try {
            return new ObjectMapper().writeValueAsString(request);
        } catch (IOException e) {
            logger.error("Error occurred converting to json");
            return "Error occurred converting to json";
        }
    }

    private void log(String url, String request) {
        logger.info(url + " " + request);
    }
}
