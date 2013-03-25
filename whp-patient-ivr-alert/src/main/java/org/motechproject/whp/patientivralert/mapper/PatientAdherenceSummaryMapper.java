package org.motechproject.whp.patientivralert.mapper;

import org.motechproject.whp.patientivralert.model.PatientAdherenceRecord;
import org.motechproject.whp.reports.contract.query.PatientAdherenceSummary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PatientAdherenceSummaryMapper {

    public List<PatientAdherenceRecord> map(List<PatientAdherenceSummary> summaries) {
        List<PatientAdherenceRecord> records = new ArrayList<>();
        for(PatientAdherenceSummary summary : summaries){
            records.add(new PatientAdherenceRecord(summary.getPatientId(), summary.getMobileNumber(), summary.getMissingWeeks()));
        }

        return records;
    }
}
