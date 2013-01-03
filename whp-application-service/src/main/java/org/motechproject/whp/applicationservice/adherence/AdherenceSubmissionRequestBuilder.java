package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.DateTime;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;

import static org.motechproject.whp.adherence.criteria.UpdateAdherenceCriteria.isWindowOpenToday;

public class AdherenceSubmissionRequestBuilder {

    public static AdherenceSubmissionRequest createAdherenceSubmissionRequest(String providerId, String submittedBy, DateTime submissionDate) {
        AdherenceSubmissionRequest request = new AdherenceSubmissionRequest();
        request.setProviderId(providerId);
        request.setSubmittedBy(submittedBy);
        request.setSubmissionDate(submissionDate.toDate());
        request.setWithinAdherenceWindow(isWindowOpenToday());
        return request;
    }
}
