package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.adherence.criteria.UpdateAdherenceCriteria;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;

public class AdherenceSubmissionRequestBuilder {

    public static AdherenceSubmissionRequest createAdherenceSubmissionRequest(String providerId, String submittedBy, DateTime submissionDate, LocalDate doseDate) {
        boolean withinCurrentAdherenceWindow = UpdateAdherenceCriteria.isWithinCurrentAdherenceWindow(doseDate);

        AdherenceSubmissionRequest request = request(providerId, submittedBy, submissionDate, withinCurrentAdherenceWindow);
        return request;
    }

    public static AdherenceSubmissionRequest createAdherenceSubmissionRequestByProvider(String providerId, DateTime submissionDate) {
        return request(providerId, providerId, submissionDate, true);
    }

    private static AdherenceSubmissionRequest request(String providerId, String submittedBy, DateTime submissionDate, boolean withinCurrentAdherenceWindow) {
        AdherenceSubmissionRequest request = new AdherenceSubmissionRequest();
        request.setProviderId(providerId);
        request.setSubmittedBy(submittedBy);
        request.setSubmissionDate(submissionDate.toDate());
        request.setWithinAdherenceWindow(withinCurrentAdherenceWindow);
        return request;
    }
}
