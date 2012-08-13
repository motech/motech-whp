package org.motechproject.whp.applicationservice.orchestrator.treatmentUpdateOrchestrator.part;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.Phase;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.motechproject.util.DateUtil.newDate;
import static org.motechproject.util.DateUtil.today;

public class DoseInterruptionsTestPart extends TreatmentUpdateOrchestratorTestPart {

    @Before
    public void setUp() {
        super.setUp();
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
