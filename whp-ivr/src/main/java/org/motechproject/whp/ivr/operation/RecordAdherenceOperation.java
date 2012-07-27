package org.motechproject.whp.ivr.operation;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.util.IvrSession;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentWeekInstance;

public class RecordAdherenceOperation implements INodeOperation {

    private WHPAdherenceService whpAdherenceService;

    private String currentPatientId;

    public RecordAdherenceOperation(WHPAdherenceService whpAdherenceService, String currentPatientId) {
        this.whpAdherenceService = whpAdherenceService;
        this.currentPatientId = currentPatientId;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        recordAdherence(userInput, ivrSession.providerId());
        ivrSession.resetCurrentPatientIndex();
    }

    private void recordAdherence(String adherenceInput, String providerId) {
        AuditParams auditParams = new AuditParams(providerId, AdherenceSource.IVR, "");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(currentPatientId, currentWeekInstance());
        weeklyAdherenceSummary.setDosesTaken(Integer.parseInt(adherenceInput));
        whpAdherenceService.recordAdherence(weeklyAdherenceSummary, auditParams);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordAdherenceOperation that = (RecordAdherenceOperation) o;

        if (currentPatientId != null ? !currentPatientId.equals(that.currentPatientId) : that.currentPatientId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return currentPatientId != null ? currentPatientId.hashCode() : 0;
    }
}
