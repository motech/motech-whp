package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.common.domain.Phase;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PhaseTransitionTestPart extends TreatmentUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Ignore
    @Test
    public void shouldOnlyEndCurrentPhaseWhenAttemptingToTransition_PatientDoseComplete_NextPhaseInfoNotSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.getCurrentTherapy().getCurrentPhase().setNumberOfDosesTaken(24, twentyFourthDoseTakenDate);
        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);

        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);
        PhaseRecord previousPhase = patient.getCurrentPhase();
        treatmentUpdateOrchestrator.adjustPhaseStartDates(patient.getPatientId(), therapyStartDate.minusDays(1), null, null);

        verify(whpAdherenceService).nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate);

        assertTrue(patient.getCurrentTherapy().getCurrentPhase().getEndDate().equals(adherenceRecord.doseDate()));
        assertTrue(previousPhase.equals(patient.getCurrentPhase()));
    }

    @Test
    public void shouldStartNextPhaseWhenAttemptingToTransition_NextPhaseSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);
        patient.nextPhaseName(Phase.EIP);
        PhaseRecord previousPhase = patient.getCurrentPhase();
        patient.endLatestPhase(twentyFourthDoseTakenDate);
        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);

        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        treatmentUpdateOrchestrator.setNextPhase(patient.getPatientId(), Phase.EIP);

        assertFalse(previousPhase.equals(patient.getCurrentPhase()));
    }

    @Test
    public void shouldRevertAutoCloseOfLastPhase_AfterItHasBeenClosed_LastPhaseBecomesNotDoseComplete_NextPhaseNotSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);
        PhaseRecord previousPhase = patient.getCurrentPhase();
        patient.endLatestPhase(twentyFourthDoseTakenDate);

        //set and reset to show the flow of a phase auto closing and then the CMF Admin manually reducing a dose
        patient.setNumberOfDosesTaken(Phase.IP, 23, twentyFourthDoseTakenDate);

        when(patientService.findByPatientId(patient.getPatientId())).thenReturn(patient);
        treatmentUpdateOrchestrator.adjustPhaseStartDates(patient.getPatientId(), therapyStartDate.minusDays(1), null, null);

        assertNotNull(patient.getCurrentPhase()); // Current phase is not closed
        assertNull(patient.getCurrentPhase().getEndDate()); // Current phase's end date is undefined as phase is still ongoing
        assertEquals(previousPhase, patient.getCurrentPhase()); // Phase transition shouldn't happen as it was reverted
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
    }
}
