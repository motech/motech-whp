package org.motechproject.whp.refdata.seed;

import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.adherence.contract.AdherenceRecord;
import org.motechproject.whp.adherence.service.AdherenceLogService;
import org.motechproject.whp.adherence.service.AdherenceRecordReportingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceRecordSeed {
    private final AdherenceLogService adherenceLogService;
    private final AdherenceRecordReportingService adherenceRecordReportingService;

    @Autowired
    public AdherenceRecordSeed(AdherenceLogService adherenceLogService, AdherenceRecordReportingService adherenceRecordReportingService) {
        this.adherenceLogService = adherenceLogService;
        this.adherenceRecordReportingService = adherenceRecordReportingService;
    }

    @Seed(priority = 0, version = "5.0")
    public void reportAdherenceRecords() {
        List<AdherenceRecord> adherenceRecords;

        int pageNumber = 1;
        do{
            adherenceRecords =  adherenceLogService.fetchAllAdherenceRecords(pageNumber++);
            reportAdherenceRecords(adherenceRecords);
        } while(auditRecordListIsNotEmpty(adherenceRecords));

    }

    private void reportAdherenceRecords(List<AdherenceRecord> adherenceRecords) {
        for(AdherenceRecord adherenceRecord: adherenceRecords){
            adherenceRecordReportingService.report(adherenceRecord);
        }
    }

    private boolean auditRecordListIsNotEmpty(List<AdherenceRecord> adherenceRecordList) {
        return adherenceRecordList != null && adherenceRecordList.size() != 0;
    }

}
