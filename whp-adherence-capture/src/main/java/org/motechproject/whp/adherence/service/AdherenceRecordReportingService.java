package org.motechproject.whp.adherence.service;

import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.domain.PillStatus;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.adherence.AdherenceRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdherenceRecordReportingService {

    private ReportingPublisherService reportingPublisherService;

    @Autowired
    public AdherenceRecordReportingService(ReportingPublisherService reportingPublisherService) {
        this.reportingPublisherService = reportingPublisherService;
    }

    public void report(AdherenceRecord adherenceRecord) {
        reportingPublisherService.reportAdherenceRecord(populateAdherenceRecord(adherenceRecord));
    }

    public void delete( List<AdherenceRecord> adherenceRecords) {
        List<AdherenceRecordDTO> adherenceRecordDTOs = new ArrayList<>();
         for (AdherenceRecord adherenceRecord : adherenceRecords) {
            AdherenceRecordDTO adherenceRecordDTO = populateAdherenceRecord(adherenceRecord);
             adherenceRecordDTOs.add(adherenceRecordDTO);

         }
        reportingPublisherService.reportAdherenceRecordDelete(adherenceRecordDTOs);
    }

    private AdherenceRecordDTO populateAdherenceRecord(AdherenceRecord adherenceRecord) {

        AdherenceRecordDTO adherenceRecordDTO = new AdherenceRecordDTO();
        adherenceRecordDTO.setDistrict(adherenceRecord.district());
        adherenceRecordDTO.setPatientId(adherenceRecord.externalId());
        adherenceRecordDTO.setPillDate(new Date(adherenceRecord.doseDate().toDate().getTime()));
        adherenceRecordDTO.setPillStatus(PillStatus.get(adherenceRecord.status()).name());
        adherenceRecordDTO.setProviderId(adherenceRecord.providerId());
        adherenceRecordDTO.setTbId(adherenceRecord.tbId());
        adherenceRecordDTO.setTherapyId(adherenceRecord.treatmentId());
        return adherenceRecordDTO;
    }
}
