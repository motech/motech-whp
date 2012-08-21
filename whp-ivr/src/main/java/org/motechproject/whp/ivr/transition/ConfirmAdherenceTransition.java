package org.motechproject.whp.ivr.transition;


import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.applicationservice.orchestrator.TreatmentUpdateOrchestrator;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.operation.RecordAdherenceOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;

import static org.motechproject.whp.ivr.prompts.ConfirmAdherencePrompts.confirmAdherencePrompts;
import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;

@EqualsAndHashCode
public class ConfirmAdherenceTransition extends TransitionToCollectPatientAdherence {

    @Autowired
    private WHPAdherenceService adherenceService;
    @Autowired
    private TreatmentUpdateOrchestrator treatmentUpdateOrchestrator;
    @Autowired
    private PatientService patientService;

    public ConfirmAdherenceTransition() {
    }

    public ConfirmAdherenceTransition(WhpIvrMessage whpIvrMessage,
                                      WHPAdherenceService adherenceService,
                                      TreatmentUpdateOrchestrator treatmentUpdateOrchestrator,
                                      ReportingPublisherService reportingPublisherService,
                                      PatientService patientService) {

        super(whpIvrMessage, reportingPublisherService);

        this.adherenceService = adherenceService;
        this.treatmentUpdateOrchestrator = treatmentUpdateOrchestrator;
        this.patientService = patientService;
        this.reportingPublisherService = reportingPublisherService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        String currentPatientId = ivrSession.currentPatientId();

        Node nextNode = new Node();
        IVRInput ivrInput = new IVRInput(input);
        if (ivrInput.noInput()) {
            repeatNode(ivrSession, currentPatientId, nextNode);
        } else {
            switch (input) {
                case "1":
                    ivrSession.recordAdherenceForCurrentPatient();
                    nextNode.addOperations(new RecordAdherenceOperation(currentPatientId, treatmentUpdateOrchestrator, reportingPublisherService));
                    addTransitionsAndPromptsForNextPatient(ivrSession, nextNode);
                    break;
                case "2":
                    addTransitionsAndPromptsForCurrentPatient(nextNode, ivrSession);
                    break;
                default:
                   repeatNode(ivrSession, currentPatientId, nextNode);
            }
        }
        return nextNode;
    }

    private void repeatNode(IvrSession ivrSession, String currentPatientId, Node nextNode) {
        Patient patient = patientService.findByPatientId(currentPatientId);
        Integer adherenceInput = Integer.parseInt(ivrSession.adherenceInputForCurrentPatient().input());
        nextNode.addPrompts(providedAdherencePrompts(whpIvrMessage, currentPatientId, adherenceInput, patient.dosesPerWeek()));
        nextNode.addPrompts(confirmAdherencePrompts(whpIvrMessage));
        nextNode.addTransition("?", new ConfirmAdherenceTransition());
    }

}
