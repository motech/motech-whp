package org.motechproject.whp.adherenceapi.response.flashing;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.model.PatientAdherenceStatus;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class AdherenceFlashingResponseTest {

    private String providerId;

    public List<PatientAdherenceStatus> patientsWithAdherence;
    public List<PatientAdherenceStatus> patientsWithoutAdherence;
    public List<PatientAdherenceStatus> patients = new ArrayList<>();

    @Before
    public void setUp() {
        providerId = "raj";
        LocalDate currentAdherenceReportWeekStartDate = currentAdherenceCaptureWeek().startDate();
        PatientAdherenceStatus patientWithAdherence1 = new PatientAdherenceStatus("patient1", currentAdherenceReportWeekStartDate);
        PatientAdherenceStatus patientWithAdherence2 = new PatientAdherenceStatus("patient2", currentAdherenceReportWeekStartDate);

        patientsWithAdherence = asList(patientWithAdherence1, patientWithAdherence2);

        PatientAdherenceStatus patientWithoutAdherence1 = new PatientAdherenceStatus("patient3", null);
        patientsWithoutAdherence = asList(patientWithoutAdherence1);

        patients.addAll(patientsWithAdherence);
        patients.addAll(patientsWithoutAdherence);
    }

    @Test
    public void testAdherenceStatusValues() {
        AdherenceSummaryByProvider adherenceSummary = new AdherenceSummaryByProvider(providerId, patients);
        AdherenceStatus adherenceStatus = new AdherenceFlashingResponse(adherenceSummary).getAdherenceStatus();

        assertEquals(2, adherenceStatus.getPatientGivenCount().intValue());
        assertEquals(1, adherenceStatus.getPatientRemainingCount().intValue());
        assertEquals(providerId, adherenceStatus.getProviderId());
        assertEquals(new PatientsRemaining(adherenceSummary.getAllPatientIdsWithoutAdherence()), adherenceStatus.getPatientsRemaining());
    }
}
