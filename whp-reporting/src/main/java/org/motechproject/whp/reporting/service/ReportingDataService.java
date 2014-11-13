package org.motechproject.whp.reporting.service;

import org.motechproject.whp.reporting.ReportingApplicationURLs;
import org.motechproject.whp.reporting.domain.DoNotCallEntrySummary;
import org.motechproject.whp.reporting.domain.ProviderAdherenceSummaries;
import org.motechproject.whp.reports.contract.query.PatientAdherenceSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.util.Arrays.asList;

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

    public List<PatientAdherenceSummary> getPatientsWithMissingAdherence(Integer skip, Integer limit) {
        return asList(restTemplate.getForObject(reportingApplicationURLs.getPatientAdherenceMissingDataURL(),
                PatientAdherenceSummary[].class,
                skip, limit));
    }
    /**
     * @author atish
     * Returning List of DoNotCallPatients
     * @return {@link List}
     */
    public List<DoNotCallEntrySummary> getDoNotCallPatientsByPatientReminderAlert() {
    	return asList(restTemplate.getForObject(reportingApplicationURLs.getDonotcallPatientsURLByPatientReminderAlert(), DoNotCallEntrySummary[].class));
    }
    
    
    /**
     * @author atish
     * Returning List of DoNotCallPatients
     * @return {@link List}
     */
    public List<DoNotCallEntrySummary> getDoNotCallPatientsByPatientIvrAlert() {
    	return asList(restTemplate.getForObject(reportingApplicationURLs.getDonotcallPatientsURLByPatientIvrAlert(), DoNotCallEntrySummary[].class));
    }
}
