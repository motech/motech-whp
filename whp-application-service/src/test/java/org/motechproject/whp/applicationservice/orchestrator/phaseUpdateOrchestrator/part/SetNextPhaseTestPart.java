package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.refdata.domain.Phase;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SetNextPhaseTestPart extends PhaseUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldNotEndCurrentPhaseWhenPatientHasNotTakenAllDosesForCurrentPhase() {
        when(patientService.setNextPhaseName(patient.getPatientId(), Phase.EIP)).thenReturn(patient);
        phaseUpdateOrchestrator.setNextPhase(PATIENT_ID, Phase.EIP);

        verify(patientService, times(1)).setNextPhaseName(patient.getPatientId(), Phase.EIP);
    }

    @Test
    public void shouldEndCurrentPhaseWhenPatientHasTakenAllDosesForCurrentPhase() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        final LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);

        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        when(patientService.setNextPhaseName(patient.getPatientId(), Phase.EIP)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                patient.nextPhaseName(Phase.EIP);
                return patient;
            }
        });

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                patient.endLatestPhase(twentyFourthDoseTakenDate);
                return null;
            }
        }).when(patientService).autoCompleteLatestPhase(patient, twentyFourthDoseTakenDate);


        phaseUpdateOrchestrator.setNextPhase(patient.getPatientId(), Phase.EIP);

        verify(patientService, times(1)).setNextPhaseName(patient.getPatientId(), Phase.EIP);
        verify(patientService, times(1)).autoCompleteLatestPhase(patient, twentyFourthDoseTakenDate);
    }

    @Test
    public void shouldSetNextPhaseAndStartNextPhase_PatientIsTransitioning() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        final LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);

        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);
        patient.nextPhaseName(Phase.EIP);

        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        when(patientService.setNextPhaseName(patient.getPatientId(), Phase.EIP)).thenReturn(patient);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                patient.endLatestPhase(twentyFourthDoseTakenDate);
                return null;
            }
        }).when(patientService).autoCompleteLatestPhase(patient, twentyFourthDoseTakenDate);

        phaseUpdateOrchestrator.setNextPhase(patient.getPatientId(), Phase.EIP);

        verify(patientService, times(1)).setNextPhaseName(patient.getPatientId(), Phase.EIP);
        verify(whpAdherenceService, times(1)).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService).startNextPhase(patient);
    }
}
