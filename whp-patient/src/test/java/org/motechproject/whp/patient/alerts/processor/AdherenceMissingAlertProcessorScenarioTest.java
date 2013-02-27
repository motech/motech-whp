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

import static junit.framework.Assert.assertEquals;

public class AdherenceMissingAlertProcessorScenarioTest extends BaseUnitTest {

    private AdherenceMissingAlertProcessor adherenceMissingAlertProcessor;
    private TreatmentWeekInstance treatmentWeekInstance;

    @Before
    public void setUp() {
        treatmentWeekInstance = TreatmentWeekInstanceBuilder.build();
        adherenceMissingAlertProcessor = new AdherenceMissingAlertProcessor(treatmentWeekInstance);
    }

    @Test
    public void shouldReturnAdherenceMissingWeeksBasedOnAdherenceWindow() {
        DoseInterruptions doseInterruptions = new DoseInterruptions();
        doseInterruptions.add(new DoseInterruption(new LocalDate(2013, 01, 01)));
        Patient patient = new PatientBuilder().withDefaults().withDoseInterruptions(doseInterruptions).build();

        mockCurrentDate(new LocalDate(2013, 01, 18)); //Friday
        assertEquals(1.0, adherenceMissingAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 25)); //Friday + 1 week
        assertEquals(2.0, adherenceMissingAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 15)); //Tue
        assertEquals(0.0, adherenceMissingAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 22)); //Tue + 1 week
        assertEquals(1.0, adherenceMissingAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAdherenceMissingWeeksBasedOnAdherenceWindow_whenAdherenceHasBeenGivenForFutureDate() {
        DoseInterruptions doseInterruptions = new DoseInterruptions();
        DoseInterruption olderDoseInterruption = new DoseInterruption(new LocalDate(2013, 01, 01));
        olderDoseInterruption.endMissedPeriod(new LocalDate(2013, 01, 24));
        doseInterruptions.add(olderDoseInterruption);
        Patient patient = new PatientBuilder().withDefaults().withDoseInterruptions(doseInterruptions).build();

        mockCurrentDate(new LocalDate(2013, 01, 18)); //Friday
        assertEquals(0.0, adherenceMissingAlertProcessor.process(patient));

        mockCurrentDate(new LocalDate(2013, 01, 25)); //Friday
        assertEquals(0.0, adherenceMissingAlertProcessor.process(patient));
    }

    @Test
    public void shouldReturnAdherenceMissingWeeksBasedOnAdherenceWindow_whenOnGoingDoseInterruptionIsAheadOfTillDate() {
        DoseInterruptions doseInterruptions = new DoseInterruptions();
        DoseInterruption currentDoseInterruption = new DoseInterruption(new LocalDate(2013, 01, 15));
        doseInterruptions.add(currentDoseInterruption);
        Patient patient = new PatientBuilder().withDefaults().withDoseInterruptions(doseInterruptions).build();

        mockCurrentDate(new LocalDate(2013, 01, 01));
        assertEquals(0.0, adherenceMissingAlertProcessor.process(patient));
    }
}
