package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import junit.framework.Assert;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.motechproject.adherence.contract.AdherenceRecord;
import org.motechproject.whp.patient.domain.PhaseRecord;
import org.motechproject.whp.refdata.domain.Phase;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class SetNextPhaseTestPart extends TreatmentUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldNotEndCurrentPhaseWhenPatientHasNotTakenAllDosesForCurrentPhase() {

        treatmentUpdateOrchestrator.setNextPhase(PATIENT_ID, Phase.EIP);

        assertNull(patient.getCurrentTherapy().getCurrentPhase().getEndDate());
    }

    @Test
    public void shouldEndCurrentPhaseWhenPatientHasTakenAllDosesForCurrentPhase() {
        LocalDate therapyStartDate = new LocalDate(2012, 3, 1);
        final LocalDate twentyFourthDoseTakenDate = new LocalDate(2012, 5, 11);
        patient.startTherapy(therapyStartDate);
        patient.setNumberOfDosesTaken(Phase.IP, 24, twentyFourthDoseTakenDate);

        AdherenceRecord adherenceRecord = new AdherenceRecord(patient.getPatientId(), THERAPY_ID, twentyFourthDoseTakenDate);
        when(whpAdherenceService.nThTakenDose(patient.getPatientId(), THERAPY_ID, 24, therapyStartDate)).thenReturn(adherenceRecord);

        PhaseRecord previousPhase = patient.getCurrentPhase();

        treatmentUpdateOrchestrator.setNextPhase(patient.getPatientId(), Phase.EIP);

        assertNotNull(previousPhase.getEndDate());
        PhaseRecord currentPhase = patient.getCurrentPhase();
        assertNotSame(previousPhase, currentPhase);

        assertNull(currentPhase.getEndDate());
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

        PhaseRecord previousPhase = patient.getCurrentPhase();
        treatmentUpdateOrchestrator.setNextPhase(patient.getPatientId(), Phase.EIP);

        assertEquals(Phase.EIP,patient.getCurrentPhase().getName());

        verify(whpAdherenceService, times(1)).nThTakenDose(anyString(), anyString(), anyInt(), any(LocalDate.class));
        assertFalse(previousPhase.equals(patient.getCurrentPhase()));
    }
}
