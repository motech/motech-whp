package org.motechproject.whp.adherence.service;

import org.junit.Test;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;

import static junit.framework.Assert.assertEquals;

public class AuditAdherenceTestPart extends WHPAdherenceServiceTestPart {
    @Test
    public void shouldLogAuditAfterRecordingAdherence() {
        createPatient(new PatientRequestBuilder().withDefaults().build());

        adherenceService.recordAdherence(new WeeklyAdherenceSummaryBuilder().build(), auditParams);
        assertEquals(1, allAuditLogs.getAll().size());
    }
}
