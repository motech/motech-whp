
package org.motechproject.whp.remarks;

import org.motechproject.whp.adherence.audit.domain.AuditLog;
import org.motechproject.whp.adherence.audit.repository.AllWeeklyAdherenceAuditLogs;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.Treatment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProviderRemarksService {

    AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs;

    @Autowired
    public ProviderRemarksService(AllWeeklyAdherenceAuditLogs allWeeklyAdherenceAuditLogs) {

        this.allWeeklyAdherenceAuditLogs = allWeeklyAdherenceAuditLogs;
    }
    public List<AuditLog> getRemarks(Patient patient) {
        List<Treatment> treatmentsUnderCurrentTherapy = patient.getCurrentTherapy().getTreatments();
        treatmentsUnderCurrentTherapy.add(patient.getCurrentTreatment());

        List<String> tbIds = new ArrayList<>();
        for(Treatment treatment : treatmentsUnderCurrentTherapy) {
            tbIds.add(treatment.getTbId());
        }

        return allWeeklyAdherenceAuditLogs.findByTbIdsWithRemarks(tbIds);

    }
}
