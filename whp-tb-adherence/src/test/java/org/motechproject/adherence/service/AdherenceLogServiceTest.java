package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.motechproject.util.DateUtil;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class AdherenceLogServiceTest {

    @Mock
    AllAdherenceLogs allAdherenceLogs;

    AdherenceLogService adherenceLogService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceLogService = new AdherenceLogService(allAdherenceLogs);
    }

    @Test
    public void shouldCountDosesTakenBetweenTwoDates() {
        LocalDate yesterday = DateUtil.today().minusDays(1);
        LocalDate today = DateUtil.today();
        String patientId = "patientId";
        String treatmentId = "treatmentId";

        adherenceLogService.countOfDosesTakenBetween(patientId, treatmentId, yesterday, today);

        verify(allAdherenceLogs).countOfDosesTakenBetween(patientId, treatmentId, yesterday, today);
    }
}
