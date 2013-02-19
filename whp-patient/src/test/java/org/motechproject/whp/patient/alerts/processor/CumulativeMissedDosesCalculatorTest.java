package org.motechproject.whp.patient.alerts.processor;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.TreatmentWeekInstance;
import org.motechproject.whp.patient.domain.Patient;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CumulativeMissedDosesCalculatorTest {

    @Mock
    TreatmentWeekInstance treatmentWeekInstance;

    @Mock
    Patient patient;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldCalculateCumulativeMissedDosesUsingTillDate(){
        LocalDate tillDate = new LocalDate(2012, 12, 12);
        when(treatmentWeekInstance.previousAdherenceWeekEndDate()).thenReturn(tillDate);
        when(patient.cumulativeMissedDoses(tillDate)).thenReturn(12);

        CumulativeMissedDosesCalculator cumulativeMissedDosesCalculator = new CumulativeMissedDosesCalculator(treatmentWeekInstance);


        assertThat(cumulativeMissedDosesCalculator.getCumulativeMissedDoses(patient), is(12));
    }
}
