package org.motechproject.whp.adherence.service;

import org.junit.Test;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;

public class AuditAdherenceTestPart extends WHPAdherenceServiceTestPart {
    @Test
    public void shouldLogAuditAfterRecordingWeeklyAdherence() {
        createPatient(new PatientRequestBuilder().withDefaults().build());

        adherenceService.recordWeeklyAdherence(new WeeklyAdherenceSummaryBuilder().build(), auditParams);
        assertEquals(1, allAuditLogs.getAll().size());
    }

    @Test
    public void shouldLogAuditAfterRecordingDailyAdherence() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());
        adherenceService.recordDailyAdherence(asList(new DailyAdherenceRequest(7, 1, 2012, 1)), patient, auditParams);
        assertEquals(1, allDailyAdherenceAuditLogs.getAll().size());

    }
}
