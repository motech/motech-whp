package org.motechproject.whp.applicationservice.adherence;

import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.reports.contract.AdherenceSubmissionRequest;

import static junit.framework.Assert.*;

public class AdherenceSubmissionRequestBuilderTest extends BaseUnitTest {

    @Test
    public void shouldCreateAdherenceSubmissionRequestForAdherenceDay() {
        String providerId = "providerId";
        String submittedBy = "submittedBy";
        DateTime date = DateTime.now().withDayOfWeek(DayOfWeek.Sunday.getValue());
        mockCurrentDate(date);

        AdherenceSubmissionRequest adherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(providerId, submittedBy, date);
        assertEquals(providerId, adherenceSubmissionRequest.getProviderId());
        assertEquals(submittedBy, adherenceSubmissionRequest.getSubmittedBy());
        assertEquals(date.toDate(), adherenceSubmissionRequest.getSubmissionDate());
        assertTrue(adherenceSubmissionRequest.isWithinAdherenceWindow());
    }

    @Test
    public void shouldCreateAdherenceSubmissionRequestForNonAdherenceDay() {
        String providerId = "providerId";
        String submittedBy = "submittedBy";
        DateTime date = DateTime.now().withDayOfWeek(DayOfWeek.Thursday.getValue());
        mockCurrentDate(date);

        AdherenceSubmissionRequest adherenceSubmissionRequest = AdherenceSubmissionRequestBuilder.createAdherenceSubmissionRequest(providerId, submittedBy, date);
        assertEquals(providerId, adherenceSubmissionRequest.getProviderId());
        assertEquals(submittedBy, adherenceSubmissionRequest.getSubmittedBy());
        assertEquals(date.toDate(), adherenceSubmissionRequest.getSubmissionDate());
        assertFalse(adherenceSubmissionRequest.isWithinAdherenceWindow());
    }
}
