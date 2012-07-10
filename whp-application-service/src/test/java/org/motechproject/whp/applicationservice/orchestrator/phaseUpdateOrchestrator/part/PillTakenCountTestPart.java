package org.motechproject.whp.applicationservice.orchestrator.phaseUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.refdata.domain.Phase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.TreatmentWeekInstance.weekInstance;

public class PillTakenCountTestPart extends PhaseUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldSummarizePillTakenCountOnPatient() {
        LocalDate today = today();
        LocalDate startDate = today.minusDays(1);
        int numberOfDosesTakenInIP = 10;
        int numberOfDosesTakenInIPTillLastSunday = 7;
        patient.startTherapy(startDate);

        when(allPatients.findByPatientId(PATIENT_ID)).thenReturn(patient);
        when(whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), THERAPY_ID, startDate, today))
                .thenReturn(numberOfDosesTakenInIP);
        when(whpAdherenceService.countOfDosesTakenBetween(patient.getPatientId(), THERAPY_ID, startDate, weekInstance(today).dateOf(DayOfWeek.Sunday)))
                .thenReturn(numberOfDosesTakenInIPTillLastSunday);

        phaseUpdateOrchestrator.recomputePillCount(patient.getPatientId());
        verify(patientService).updatePillTakenCount(patient, Phase.IP, numberOfDosesTakenInIP, numberOfDosesTakenInIPTillLastSunday);
    }
}
