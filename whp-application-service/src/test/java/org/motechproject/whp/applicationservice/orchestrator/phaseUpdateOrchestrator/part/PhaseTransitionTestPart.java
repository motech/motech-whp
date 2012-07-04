package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.refdata.domain.PhaseName;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class PhaseTransitionTestPart extends PhaseUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldOnlyEndCurrentPhaseWhenAttemptingToTransition_PatientDoseComplete_NextPhaseInfoNotSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.getCurrentTherapy().getCurrentPhase().setNumberOfDosesTaken(24);
        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(whpAdherenceService).nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate);
        verify(patientService).endCurrentPhase(patient.getPatientId(), adherenceRecord.doseDate());
        verify(patientService, never()).startNextPhase(anyString());
    }

    @Test
    public void shouldStartNextPhaseWhenAttemptingToTransition_NextPhaseSet() {
        patient.getCurrentTherapy().getPhases().setNextPhaseName(PhaseName.EIP);
        patient.getCurrentTreatment().getTherapy().setUid(THERAPY_ID);
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.getCurrentTherapy().getCurrentPhase().setEndDate(twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(patientService).startNextPhase(patient.getPatientId());
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService, never()).endCurrentPhase(anyString(), any(LocalDate.class));
    }
}
