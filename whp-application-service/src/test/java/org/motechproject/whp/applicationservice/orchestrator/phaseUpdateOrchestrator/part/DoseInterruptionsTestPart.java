package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.adherence.domain.PillStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.motechproject.util.DateUtil.today;

public class DoseInterruptionsTestPart extends PhaseUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldUpdateDoseInterruptionForPatientOnPillStatusRecompute() {
        HashMap<LocalDate,PillStatus> dateAdherenceMap = new HashMap<>();

        //no logs present for these dates
        List<LocalDate> doseDates = patient.getDoseDatesTill(today().minusDays(5));
        //mocking that taken logs present for these dates
        List<LocalDate> remainingDoseDates = new ArrayList<>();

        for (LocalDate date : patient.getDoseDatesTill(today())) {
            if (date.isAfter(doseDates.get(doseDates.size() - 1))){
                remainingDoseDates.add(date);
                dateAdherenceMap.put(date, PillStatus.Taken);
            }
        }

        when(whpAdherenceService.getDateAdherenceMap(patient)).thenReturn(dateAdherenceMap);

        phaseUpdateOrchestrator.recomputePillStatus(PATIENT_ID);

        verify(patientService, times(1)).clearDoseInterruptionsForUpdate(patient);

        for (LocalDate date : doseDates) {
            verify(patientService).dosesMissedSince(patient, date);
        }

        for (LocalDate date : remainingDoseDates) {
            verify(patientService).dosesResumedOnAfterBeingInterrupted(patient, date);
        }
    }
}
