package org.motechproject.whp.ivr.transition;

import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.builder.node.ConfirmAdherenceNodeBuilder;
import org.motechproject.whp.ivr.operation.InvalidAdherenceOperation;
import org.motechproject.whp.ivr.operation.SkipAdherenceOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.motechproject.whp.reporting.service.ReportingPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.whp.ivr.prompts.InvalidAdherencePrompts.invalidAdherencePrompts;

@Component
@EqualsAndHashCode
public class AdherenceCaptureTransition extends TransitionToCollectPatientAdherence {

    @Autowired
    private PatientService patientService;

    public AdherenceCaptureTransition() {
    }

    public AdherenceCaptureTransition(WhpIvrMessage whpIvrMessage, PatientService patientService, ReportingPublisherService reportingPublisherService) {
        super(whpIvrMessage, reportingPublisherService);
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        IVRInput ivrInput = new IVRInput(input);
        Patient patient = patientService.findByPatientId(ivrSession.currentPatientId());

        Node nextNode = new Node();
        if (ivrInput.isSkipInput()) {
            nextNode.addOperations(new SkipAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
            addTransitionsToNextPatients(ivrSession, nextNode);
        } else {
            if (patient.isValidDose(ivrInput.input())) {
                nextNode = new ConfirmAdherenceNodeBuilder(whpIvrMessage).with(patient, parseInt(input)).node();
            } else {
                nextNode.addPrompts(invalidAdherencePrompts(whpIvrMessage, patient.getCurrentTherapy().getTreatmentCategory()));
                nextNode.addOperations(new InvalidAdherenceOperation(ivrSession.currentPatientId(), reportingPublisherService));
                addPatientPromptsAndTransitions(nextNode, ivrSession);
            }
        }
        return nextNode;
    }

}
