package org.motechproject.whp.reporting.service;

import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reporting.domain.ProviderAdherenceSummaries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ReportingDataService {

    private ReportingApplicationURLs reportingApplicationURLs;
    private RestTemplate restTemplate;

    @Autowired
    public ReportingDataService(ReportingApplicationURLs reportingApplicationURLs, RestTemplate restTemplate) {
        this.reportingApplicationURLs = reportingApplicationURLs;
        this.restTemplate = restTemplate;
    }

    public ProviderAdherenceSummaries getProviderAdherenceStatus(String district) {
        return restTemplate.getForObject(reportingApplicationURLs.getProviderAdherenceStatusByDistrictURL(),
                ProviderAdherenceSummaries.class,
                district);
    }
}
