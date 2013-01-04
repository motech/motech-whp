package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;

import static junit.framework.Assert.*;
import static org.motechproject.util.DateUtil.today;

public class AdherenceSubmissionRequestBuilderTest extends BaseUnitTest {

    @Test
    public void shouldCreateAdherenceSubmissionRequestForAdherenceDay() {
        String providerId = "providerId";
        String submittedBy = "submittedBy";
        LocalDate date = DateTime.now().withDayOfWeek(DayOfWeek.Sunday.getValue()).toLocalDate();
        mockCurrentDate(date);

        AdherenceSubmissionRequest adherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(providerId, submittedBy, date, date);
        assertEquals(providerId, adherenceSubmissionRequest.getProviderId());
        assertEquals(submittedBy, adherenceSubmissionRequest.getSubmittedBy());
        assertEquals(date.toDate(), adherenceSubmissionRequest.getSubmissionDate());
        assertTrue(adherenceSubmissionRequest.isWithinAdherenceWindow());
    }

    @Test
    public void shouldCreateAdherenceSubmissionRequestForNonAdherenceDay() {
        String providerId = "providerId";
        String submittedBy = "submittedBy";
        LocalDate date = DateTime.now().withDayOfWeek(DayOfWeek.Thursday.getValue()).toLocalDate();
        mockCurrentDate(date);

        AdherenceSubmissionRequest adherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(providerId, submittedBy, date, date);
        assertEquals(providerId, adherenceSubmissionRequest.getProviderId());
        assertEquals(submittedBy, adherenceSubmissionRequest.getSubmittedBy());
        assertEquals(date.toDate(), adherenceSubmissionRequest.getSubmissionDate());
        assertFalse(adherenceSubmissionRequest.isWithinAdherenceWindow());
    }

    @Test
    public void shouldCreateAdherenceSubmissionRequestByProvider() {
        mockCurrentDate(today());
        String providerId = "provider";

        AdherenceSubmissionRequest request = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequestByProvider(providerId, today());

        assertEquals(providerId, request.getProviderId());
        assertEquals(providerId, request.getSubmittedBy());
        assertEquals(today().toDate(), request.getSubmissionDate());
        assertTrue(request.isWithinAdherenceWindow());
    }
}
