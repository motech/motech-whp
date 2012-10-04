package org.motechproject.whp.adherence.it.adherence.service;

import org.junit.Test;
import org.motechproject.whp.adherence.builder.WeeklyAdherenceSummaryBuilder;
import org.motechproject.whp.adherence.domain.AdherenceList;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.mapping.AdherenceListMapper;
import org.motechproject.whp.adherence.request.DailyAdherenceRequest;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.builder.PatientRequestBuilder;
import org.motechproject.whp.patient.domain.Patient;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.adherence.criteria.TherapyStartCriteria.shouldStartOrRestartTreatment;

public class AuditAdherenceTestPart extends WHPAdherenceServiceTestPart {
    @Test
    public void shouldLogAuditAfterRecordingWeeklyAdherence() {
        Patient patient = createPatient(new PatientRequestBuilder().withDefaults().build());

        final WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummaryBuilder().build();
        AdherenceList adherenceList = AdherenceListMapper.map(patient, weeklyAdherenceSummary);
        if (shouldStartOrRestartTreatment(patient, weeklyAdherenceSummary)) {
            patient.startTherapy(adherenceList.firstDoseTakenOn());
        }
        adherenceService.recordWeeklyAdherence(adherenceList, weeklyAdherenceSummary, patient, auditParams);
        assertEquals(1, allWeeklyAdherenceAuditLogs.getAll().size());
    }

    @Test
    public void shouldLogAuditAfterRecordingDailyAdherence() {
        Patient patient = new PatientBuilder().withDefaults().build();
        adherenceService.recordDailyAdherence(asList(new DailyAdherenceRequest(7, 1, 2012, 1)), patient, auditParams);
        assertEquals(1, allDailyAdherenceAuditLogs.getAll().size());

    }
}
