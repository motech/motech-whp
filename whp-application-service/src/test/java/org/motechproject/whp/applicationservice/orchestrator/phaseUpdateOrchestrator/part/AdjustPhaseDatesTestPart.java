package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.motechproject.whp.refdata.domain.PhaseName.*;

public class AdjustPhaseDatesTestPart extends PhaseUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldAdjustPhaseStartDatesForPatient() {
        LocalDate ipStartDate = new LocalDate(2011, 1, 1);
        LocalDate eipStartDate = new LocalDate(2011, 1, 2);
        LocalDate cpStartDate = new LocalDate(2011, 1, 3);
        patient.getCurrentTherapy().getPhase(IP).setNumberOfDosesTaken(24);
        patient.getCurrentTherapy().getPhase(EIP).setNumberOfDosesTaken(32);
        patient.getCurrentTherapy().getPhase(CP).setNumberOfDosesTaken(32);

        phaseUpdateOrchestrator.adjustPhaseStartDates(PATIENT_ID, ipStartDate, eipStartDate, cpStartDate);
        assertEquals(ipStartDate, patient.getCurrentTherapy().getPhase(IP).getStartDate());
        assertEquals(eipStartDate, patient.getCurrentTherapy().getPhase(EIP).getStartDate());
        assertEquals(cpStartDate, patient.getCurrentTherapy().getPhase(CP).getStartDate());
        verify(allPatients).update(patient);
    }

    @Test
    public void shouldRecomputePillCount() {
        LocalDate ipStartDate = new LocalDate(2011, 1, 1);
        LocalDate eipStartDate = new LocalDate(2011, 1, 2);
        LocalDate cpStartDate = new LocalDate(2011, 1, 3);
        patient.getCurrentTherapy().getPhase(IP).setNumberOfDosesTaken(24);
        patient.getCurrentTherapy().getPhase(EIP).setNumberOfDosesTaken(32);
        patient.getCurrentTherapy().getPhase(CP).setNumberOfDosesTaken(33);

        phaseUpdateOrchestrator.adjustPhaseStartDates(PATIENT_ID, ipStartDate, eipStartDate, cpStartDate);
        verify(patientService).updatePillTakenCount(eq(patient), eq(IP), anyInt());
        verify(patientService).updatePillTakenCount(eq(patient), eq(EIP), anyInt());
        verify(patientService).updatePillTakenCount(eq(patient), eq(CP), anyInt());
    }
}
