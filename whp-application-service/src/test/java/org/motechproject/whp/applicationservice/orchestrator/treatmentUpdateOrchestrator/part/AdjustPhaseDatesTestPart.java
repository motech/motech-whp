package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.refdata.domain.Phase;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.motechproject.whp.refdata.domain.Phase.*;

public class AdjustPhaseDatesTestPart extends TreatmentUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldAdjustPhaseStartDatesForPatient() {
        LocalDate ipStartDate = new LocalDate(2011, 1, 1);
        LocalDate eipStartDate = new LocalDate(2011, 1, 2);
        LocalDate cpStartDate = new LocalDate(2011, 1, 3);

        treatmentUpdateOrchestrator.adjustPhaseStartDates(PATIENT_ID, ipStartDate, eipStartDate, cpStartDate);
        assertEquals(ipStartDate, patient.getCurrentTherapy().getPhaseStartDate(IP));
        assertEquals(eipStartDate, patient.getCurrentTherapy().getPhaseStartDate(EIP));
        assertEquals(cpStartDate, patient.getCurrentTherapy().getPhaseStartDate(CP));
        verify(patientService, times(3)).update(patient);
    }

    @Test
    public void shouldRecomputePillCount() {
        LocalDate ipStartDate = new LocalDate(2011, 1, 1);
        LocalDate eipStartDate = new LocalDate(2011, 1, 2);
        LocalDate cpStartDate = new LocalDate(2011, 1, 3);

        treatmentUpdateOrchestrator.adjustPhaseStartDates(PATIENT_ID, ipStartDate, eipStartDate, cpStartDate);
        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.IP));
        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.EIP));
        assertEquals(0, patient.getCurrentTherapy().getNumberOfDosesTaken(Phase.CP));
    }
}
