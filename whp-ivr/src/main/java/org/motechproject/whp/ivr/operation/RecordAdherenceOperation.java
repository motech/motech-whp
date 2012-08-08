package org.motechproject.whp.ivr.operation;

import org.apache.log4j.Logger;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.INodeOperation;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.audit.contract.AuditParams;
import org.motechproject.whp.adherence.domain.AdherenceSource;
import org.motechproject.whp.adherence.domain.WeeklyAdherenceSummary;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.motechproject.whp.reports.contract.AdherenceCaptureRequest;

import static org.motechproject.whp.common.domain.TreatmentWeekInstance.currentAdherenceCaptureWeek;
import static org.motechproject.whp.ivr.builder.request.AdherenceCaptureBuilder.adherenceCapture;

public class RecordAdherenceOperation implements INodeOperation {

    private String currentPatientId;
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    private ReportingPublisherService reportingService;
    private static Logger logger = Logger.getLogger(RecordAdherenceOperation.class);

    public RecordAdherenceOperation(String currentPatientId, TreatmentUpdateOrchestrator treatmentUpdateOrchestrator, ReportingPublisherService reportingService) {
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.reportingService = reportingService;
        this.currentPatientId = currentPatientId;
    }

    @Override
    public void perform(String userInput, FlowSession session) {
        IvrSession ivrSession = new IvrSession(session);
        recordAdherence(ivrSession);
        publishAdherenceSubmissionEvent(ivrSession);
        updateAdherenceCapturedTime(ivrSession);
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

    private void recordAdherence(IvrSession ivrSession) {
        AuditParams auditParams = new AuditParams(ivrSession.providerId(), AdherenceSource.IVR, "");
        WeeklyAdherenceSummary weeklyAdherenceSummary = new WeeklyAdherenceSummary(currentPatientId, currentAdherenceCaptureWeek());
        weeklyAdherenceSummary.setDosesTaken(ivrSession.adherenceInputForCurrentPatient().input());
        treatmentUpdateOrchestrator.recordWeeklyAdherence(weeklyAdherenceSummary, currentPatientId, auditParams);
    }

    private void publishAdherenceSubmissionEvent(IvrSession ivrSession) {
        logger.info("Building adherence submission request");
        AdherenceCaptureRequest request = adherenceCapture().validAdherence(currentPatientId, ivrSession);
        logger.info("Publishing adherence submission request");
        reportingService.reportAdherenceCapture(request);
    }

    private void updateAdherenceCapturedTime(IvrSession ivrSession) {
        ivrSession.startOfAdherenceSubmission(DateUtil.now());
    }
}
