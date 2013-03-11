package org.motechproject.whp.remarks;


import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Therapy;
import org.motechproject.whp.patient.domain.Treatment;
import org.motechproject.whp.patient.domain.PatientType;
import org.motechproject.whp.patient.domain.TreatmentOutcome;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProviderRemarksServiceTest {
    ProviderRemarksService providerRemarksService;

    @Mock
    AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;

    @Before
    public void setUp() {
        initMocks(this);
        providerRemarksService = new ProviderRemarksService(allWeeklyAdherenceAuditLogs);
    }

    @Test
    public void shouldGetRemarksUnderCurrentTherapy() {
        String tbId1 = "tbid1";
        Treatment treatmentUnderCurrentTherapy1 = new Treatment("provider1", "providerDistrict", tbId1, PatientType.New);
        String tbId2 = "tbid2";
        Treatment treatmentUnderCurrentTherapy2 = new Treatment("provider2", "providerDistrict", tbId2, PatientType.TreatmentFailure);
        Treatment treatmentUnderOldTherapy1 = new Treatment("provider1", "providerDistrict", "tbId3", PatientType.New);

        Patient patient = new Patient();
        DateTime now = DateUtil.now();
        patient.addTreatment(treatmentUnderOldTherapy1, new Therapy(), now.minusMonths(2), now.minusMonths(2));
        patient.closeCurrentTreatment(TreatmentOutcome.Died, null, now.minusMonths(1));

        patient.addTreatment(treatmentUnderCurrentTherapy1, new Therapy(), now.minusDays(15), now.minusDays(15));
        patient.addTreatment(treatmentUnderCurrentTherapy2,now, now);

        List<AuditLog> auditLogs = mock(List.class);
        when(allWeeklyAdherenceAuditLogs.findByTbIdsWithRemarks(any(List.class))).thenReturn(auditLogs);

        List<AuditLog> remarks = providerRemarksService.getRemarks(patient);

        assertThat(remarks, is(auditLogs));

        ArgumentCaptor<List> argumentCaptor  = ArgumentCaptor.forClass(List.class);
        verify(allWeeklyAdherenceAuditLogs).findByTbIdsWithRemarks(argumentCaptor.capture());

        List<String> tbIds = argumentCaptor.getValue();
        assertThat(tbIds, hasItem(tbId1));
        assertThat(tbIds, hasItem(tbId2));


    }
}
