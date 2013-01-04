package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.now;

public class AdherenceSubmissionRequestBuilderTest extends BaseUnitTest {

    @Test
    public void shouldCreateAdherenceSubmissionRequestForAdherenceDay() {
        String providerId = "providerId";
        String submittedBy = "submittedBy";
        LocalDate date = now().withDayOfWeek(DayOfWeek.Sunday.getValue()).toLocalDate();
        mockCurrentDate(date);
        AdherenceSubmissionRequest adherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(providerId, submittedBy, now(), date);
        assertEquals(providerId, adherenceSubmissionRequest.getProviderId());
        assertEquals(submittedBy, adherenceSubmissionRequest.getSubmittedBy());
        assertEquals(now().toDate(), adherenceSubmissionRequest.getSubmissionDate());
        assertTrue(adherenceSubmissionRequest.isWithinAdherenceWindow());
    }

    @Test
    public void shouldCreateAdherenceSubmissionRequestForNonAdherenceDay() {
        String providerId = "providerId";
        String submittedBy = "submittedBy";
        mockCurrentDate(DateTime.now());
        LocalDate date = DateTime.now().withDayOfWeek(DayOfWeek.Thursday.getValue()).toLocalDate();

        AdherenceSubmissionRequest adherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(providerId, submittedBy, now(), date);
        assertEquals(providerId, adherenceSubmissionRequest.getProviderId());
        assertEquals(submittedBy, adherenceSubmissionRequest.getSubmittedBy());
        assertEquals(now().toDate(), adherenceSubmissionRequest.getSubmissionDate());
        assertFalse(adherenceSubmissionRequest.isWithinAdherenceWindow());
    }

    @Test
    public void shouldCreateAdherenceSubmissionRequestByProvider() {
        mockCurrentDate(now());
        String providerId = "provider";

        AdherenceSubmissionRequest request = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequestByProvider(providerId, now());

        assertEquals(providerId, request.getProviderId());
        assertEquals(providerId, request.getSubmittedBy());
        assertEquals(now().toDate(), request.getSubmissionDate());
        assertTrue(request.isWithinAdherenceWindow());
    }
}
