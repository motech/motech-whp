package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;
import org.motechproject.whp.common.builder.TreatmentWeekInstanceBuilder;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.DoseInterruption;
import org.motechproject.whp.patient.domain.DoseInterruptions;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.common.domain.AllDaysOfWeek.allDaysOfWeek;

public class CumulativeMissedDosesAlertProcessorScenarioTest extends BaseUnitTest {

    private CumulativeMissedDosesAlertProcessor cumulativeMissedDosesAlertProcessor;
    private TreatmentWeekInstance treatmentWeekInstance;

    @Before
    public void setUp() {
        treatmentWeekInstance = TreatmentWeekInstanceBuilder.build();
        cumulativeMissedDosesAlertProcessor = new CumulativeMissedDosesAlertProcessor(treatmentWeekInstance);
    }

    @Test
    public void shouldReturnCumulativeMissedDosesTillGivenDateBasedOnAdherenceCaptureWeek() {
        DoseInterruptions doseInterruptions = new DoseInterruptions();
        LocalDate treatmentStartDate = new LocalDate(2013, 01, 01);
        doseInterruptions.add(new DoseInterruption(treatmentStartDate));

        TreatmentCategory treatmentCategory = new TreatmentCategory("Commercial/Private Category 1", "11", 7, 8, 56, 4, 28, 18, 126, allDaysOfWeek);
        Patient patient = new PatientBuilder().withDefaults()
                .withTherapyStartDate(treatmentStartDate)
                .withCurrentTreatmentStartDate(treatmentStartDate)
                .withDoseInterruptions(doseInterruptions)
                .withTreatmentCategory(treatmentCategory).build();

        mockCurrentDate(new LocalDate(2013, 01, 18)); //Friday
        assertEquals(13, cumulativeMissedDosesAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 25)); //Friday + 1 week
        assertEquals(20, cumulativeMissedDosesAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 15)); //Tue
        assertEquals(6, cumulativeMissedDosesAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 22)); //Tue + 1 week
        assertEquals(13, cumulativeMissedDosesAlertProcessor.process(patient));
    }

}
