package org.motechproject.whp.adherence.mapping;

import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.whp.adherence.domain.Adherence;
import org.motechproject.whp.adherence.domain.AdherenceLog;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.adherence.contract.AdherenceRecords.AdherenceRecord;

public class AdherenceMapper {

    AdherenceRecords adherenceRecords;

    public AdherenceMapper(AdherenceRecords adherenceRecords) {
        this.adherenceRecords = adherenceRecords;
    }

    public Adherence map() {
        List<AdherenceLog> adherenceLogs = new ArrayList<AdherenceLog>();
        for (AdherenceRecord adherenceRecord : adherenceRecords.adherenceRecords()) {
            AdherenceRecordMapper adherenceRecordMapper = new AdherenceRecordMapper(adherenceRecord);
            AdherenceLog log = adherenceRecordMapper.adherenceLog();
            adherenceLogs.add(log);
        }
        return new Adherence(adherenceLogs);
    }
}
