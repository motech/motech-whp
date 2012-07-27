package org.motechproject.whp.ivr.operation;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.util.IvrSession;
import org.springframework.beans.factory.annotation.Autowired;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

public class RecordAdherenceOperation implements INodeOperation {

    @Autowired
    private WHPAdherenceService whpAdherenceService;

    public RecordAdherenceOperation() {
    }

    public RecordAdherenceOperation(WHPAdherenceService whpAdherenceService) {
        this.whpAdherenceService = whpAdherenceService;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        recordAdherence(userInput, ivrSession.currentPatientId(), ivrSession.providerId());
    }

    private void recordAdherence(String adherenceInput, String currentPatientId, String providerId) {
        AuditParams auditParams = new AuditParams(providerId, AdherenceSource.IVR, "");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(currentPatientId, currentWeekInstance());
        weeklyAdherenceSummary.setDosesTaken(Integer.parseInt(adherenceInput));
        whpAdherenceService.recordAdherence(weeklyAdherenceSummary, auditParams);
    }
}
