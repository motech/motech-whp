package org.motechproject.whp.adherence.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceRecordDTO;

import java.sql.Date;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.util.DateUtil.today;

public class AdherenceRecordReportingServiceTest {

    AdherenceRecordReportingService adherenceRecordReportingService;
    @Mock
    ReportingPublisherService reportingPublisherService;

    @Before
    public void setUp() {
        initMocks(this);
        adherenceRecordReportingService = new AdherenceRecordReportingService(reportingPublisherService);
    }

    @Test
    public void shouldReportAdherenceRecord() {
        AdherenceRecord adherenceRecord = new AdherenceRecord("externalId1", "treatmentId", today());
        adherenceRecord.status(PillStatus.NotTaken.getStatus());
        adherenceRecord.district("district");
        adherenceRecord.providerId("providerId");
        adherenceRecord.tbId("tbId");

        adherenceRecordReportingService.report(adherenceRecord);

        AdherenceRecordDTO expectedAdherenceRecordDTO = new AdherenceRecordDTO();
        expectedAdherenceRecordDTO.setDistrict(adherenceRecord.district());
        expectedAdherenceRecordDTO.setPatientId(adherenceRecord.externalId());
        expectedAdherenceRecordDTO.setPillDate(new Date(adherenceRecord.doseDate().toDate().getTime()));
        expectedAdherenceRecordDTO.setPillStatus(PillStatus.get(adherenceRecord.status()).name());
        expectedAdherenceRecordDTO.setProviderId(adherenceRecord.providerId());
        expectedAdherenceRecordDTO.setTbId(adherenceRecord.tbId());
        expectedAdherenceRecordDTO.setTherapyId(adherenceRecord.treatmentId());

        verify(reportingPublisherService).reportAdherenceRecord(expectedAdherenceRecordDTO);
    }

}
