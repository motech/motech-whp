package org.motechproject.whp.reporting.gateway;

import org.motechproject.whp.reporting.request.AdherenceCaptureRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Properties;

@Service
public class ReportingGateway  {

    private RestTemplate restTemplate;
    private Properties whpProperties;
    private final static Logger logger = LoggerFactory.getLogger(ReportingGateway.class);
    private static final String CAPTURE_ADHERENCE_PATH = "adherence/capture";

    @Autowired
    public ReportingGateway(RestTemplate whpRestTemplate, @Qualifier("whpProperties") Properties whpProperties) {
        this.restTemplate = whpRestTemplate;
        this.whpProperties = whpProperties;
    }

    public void captureAdherence(AdherenceCaptureRequest adherenceCaptureRequest) {
        String url = String.format("%s%s", getBaseUrl(), CAPTURE_ADHERENCE_PATH);
        try {
            restTemplate.postForLocation(url, adherenceCaptureRequest, String.class, new HashMap<String, String>());
        } catch (HttpClientErrorException ex) {
            logger.error(String.format("Reporting adherence creation failed with errorCode: %s, error: %s", ex.getStatusCode(), ex.getResponseBodyAsString()));
            throw ex;
        }
    }

    private String getBaseUrl() {
        String baseUrl = whpProperties.getProperty("reporting.service.base.url");
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }
}
