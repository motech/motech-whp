package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceServiceTest {

    @Mock
    AllAdherenceLogs allAdherenceLogs;

    AdherenceService adherenceService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceService = new AdherenceService(allAdherenceLogs);
    }

    @Test
    public void shouldCountDosesTakenBetweenTwoDates() {
        LocalDate yesterday = DateUtil.today().minusDays(1);
        LocalDate today = DateUtil.today();
        String patientId = "patientId";
        String treatmentId = "treatmentId";

        adherenceService.countOfDosesTakenBetween(patientId, treatmentId, yesterday, today);

        verify(allAdherenceLogs).countOfDosesTakenBetween(patientId, treatmentId, yesterday, today);
    }
}
