package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.Phase;

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
        patient.getCurrentTherapy().getCurrentPhase().setNumberOfDosesTaken(24, twentyFourthDoseTakenDate);
        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(whpAdherenceService).nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate);
        verify(patientService).autoCompleteCurrentPhase(patient, adherenceRecord.doseDate());
        verify(patientService, never()).startNextPhase(any(Patient.class));
    }

    @Test
    public void shouldStartNextPhaseWhenAttemptingToTransition_NextPhaseSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);
        patient.nextPhaseName(Phase.EIP);
        patient.endCurrentPhase(twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(patientService).startNextPhase(patient);
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService, never()).autoCompleteCurrentPhase(any(Patient.class), any(LocalDate.class));
    }

    @Test
    public void shouldRevertAutoCloseOfLastPhase_AfterItHasBeenClosed_LastPhaseBecomesNotDoseComplete_NextPhaseNotSet() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);
        patient.endCurrentPhase(twentyFourthDoseTakenDate);

        //set and reset to show the flow of a phase auto closing and then the CMF Admin manually reducing a dose
        patient.setNumberOfDosesTaken(Phase.IP, 23, twentyFourthDoseTakenDate);

        when(allPatients.findByPatientId(patient.getPatientId())).thenReturn(patient);

        phaseUpdateOrchestrator.attemptPhaseTransition(patient.getPatientId());

        verify(patientService, times(1)).revertAutoCompleteOfLastPhase(patient);
        verify(patientService, never()).startNextPhase(patient);
        verify(whpAdherenceService, never()).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        verify(patientService, never()).autoCompleteCurrentPhase(any(Patient.class), any(LocalDate.class));
    }
}
