package org.motechproject.whp.adherence.report;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.model.DayOfWeek;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.service.WHPAdherenceService;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceReportBuilderTest {

    Adherence adherence;

    @Mock
    WHPAdherenceService adherenceService;

    AdherenceReportBuilder adherenceReportBuilder = new AdherenceReportBuilder(adherenceService);

    @Before
    public void setup() {
        initMocks(this);
        adherence = new Adherence("patientID", "treatmentId", DayOfWeek.Monday, DateUtil.today());
        adherence.setTbId("tbId");
        adherence.setProviderId("providerId");
    }

    @Test
    public void shouldPassPatientIdToReport() {
        assertTrue(adherenceReportBuilder.createRowData(adherence).contains(adherence.getPatientId()));
    }

    @Test
    public void shouldPassTbIdToReport() {
        assertTrue(adherenceReportBuilder.createRowData(adherence).contains(adherence.getTbId()));
    }

    @Test
    public void shouldPassDateOfAdherenceToReport() {
        assertTrue(adherenceReportBuilder.createRowData(adherence).contains(adherence.getPillDate().toString("dd/MM/yyyy")));
    }

    @Test
    public void shouldPassAdherenceValueToReport() {
        assertTrue(adherenceReportBuilder.createRowData(adherence).contains(adherence.getPillStatus().name()));
    }
}
