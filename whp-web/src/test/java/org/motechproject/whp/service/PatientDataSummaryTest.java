package org.motechproject.whp.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.service.PatientService;

import java.util.List;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;

public class PatientDataSummaryTest {

    @Mock
    private PatientService patientService;

    private PatientDataSummary patientDataSummary;

    @Before
    public void setup() {
        initMocks(this);
        patientDataSummary = new PatientDataSummary(patientService);
    }

    @Test
    public void shouldRetrieveReportDataInPages() {
        patientDataSummary.patientSummaryReport(1);
        verify(patientService).getAll(0, 10000);

        patientDataSummary.patientSummaryReport(2);
        verify(patientService).getAll(1, 10000);

        patientDataSummary.patientSummaryReport(20);
        verify(patientService).getAll(19, 10000);
    }

    @Test
    public void shouldRetrieveReportHeader_forCurrentDate() {
        List<String> results = patientDataSummary.patientSummaryHeader();
        assertTrue(results.get(0).contains(new WHPDate(today()).value()));
        assertTrue(results.get(1).contains(new WHPDate(currentAdherenceCaptureWeek().dateOf(DayOfWeek.Sunday)).value()));
    }
}
