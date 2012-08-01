package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.newDate;
import static org.motechproject.util.DateUtil.today;

public class DoseInterruptionsTestPart extends PhaseUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldUpdateDoseInterruptionForPatientOnPillStatusRecompute() {
        HashMap<LocalDate, PillStatus> dateAdherenceMap = new HashMap<>();

        //no logs present for these dates
        List<LocalDate> doseDates = patient.getDoseDatesTill(today().minusDays(5));
        //mocking that taken logs present for these dates
        List<LocalDate> remainingDoseDates = new ArrayList<>();

        for (LocalDate date : patient.getDoseDatesTill(today())) {
            if (date.isAfter(doseDates.get(doseDates.size() - 1))) {
                remainingDoseDates.add(date);
                dateAdherenceMap.put(date, PillStatus.Taken);
            }
        }

        when(whpAdherenceService.getDateAdherenceMap(patient)).thenReturn(dateAdherenceMap);

        treatmentUpdateOrchestrator.recomputePillStatus(patient);

        verify(patientService, times(1)).clearDoseInterruptionsForUpdate(patient);

        for (LocalDate date : doseDates) {
            verify(patientService).dosesMissedSince(patient, date);
        }

        for (LocalDate date : remainingDoseDates) {
            verify(patientService).dosesResumedOnAfterBeingInterrupted(patient, date);
        }
    }

    @Test
    public void shouldGetDosesOnlyTillCPEndDateIfSet() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.startTherapy(newDate(2012, 3, 1));
        patient.nextPhaseName(Phase.CP);
        patient.endLatestPhase(newDate(2012, 3, 31));
        patient.startNextPhase();
        patient.setNumberOfDosesTaken(Phase.CP, 1, newDate(2012, 4, 30));
        patient.endLatestPhase(newDate(2012, 4, 30));

        List<LocalDate> doseDates = patient.getDoseDatesTill(today());
        assertEquals(26, doseDates.size());
        assertEquals(newDate(2012, 3, 2), doseDates.get(0));
        assertEquals(newDate(2012, 4, 30), doseDates.get(doseDates.size() - 1));
    }

}
