package org.motechproject.whp.adherenceapi.response.flashing;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherence.domain.AdherenceSummaryByProvider;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class AdherenceFlashingResponseTest {

    private String providerId;

    public List<Patient> patientsWithAdherence;
    public List<Patient> patientsWithoutAdherence;
    public List<Patient> patients = new ArrayList<>();

    @Before
    public void setUp() {
        providerId = "raj";
        Patient patientWithAdherence1 = new PatientBuilder().withDefaults().withPatientId("patient1").withTherapyStartDate(new LocalDate(2012,7,7)).withAdherenceProvidedForLastWeek().build();
        Patient patientWithAdherence2 = new PatientBuilder().withDefaults().withPatientId("patient2").withTherapyStartDate(new LocalDate(2012,7,7)).withAdherenceProvidedForLastWeek().build();

        patientsWithAdherence = asList(patientWithAdherence1, patientWithAdherence2);

        Patient patientWithoutAdherence1 = new PatientBuilder().withDefaults().withPatientId("patient3").build();
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
