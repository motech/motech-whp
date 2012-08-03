package org.motechproject.whp.ivr.transition;

import lombok.EqualsAndHashCode;
import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.adherence.service.AdherenceDataService;
import org.motechproject.whp.ivr.IVRInput;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.node.ConfirmAdherenceNode;
import org.motechproject.whp.ivr.operation.ResetPatientIndexOperation;
import org.motechproject.whp.ivr.session.IvrSession;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;
import static org.motechproject.util.DateUtil.now;

@Component
@EqualsAndHashCode
public class AdherenceCaptureTransition extends TransitionToCollectPatientAdherence {

    @Autowired
    private PatientService patientService;

    AdherenceCaptureTransition() {
    }

    public AdherenceCaptureTransition(WHPIVRMessage whpivrMessage, AdherenceDataService adherenceDataService, PatientService patientService) {
        super(whpivrMessage, adherenceDataService);
        this.patientService = patientService;
    }

    @Override
    public Node getDestinationNode(String input, FlowSession flowSession) {
        IvrSession ivrSession = new IvrSession(flowSession);
        IVRInput ivrInput = new IVRInput(input);
        Patient patient = patientService.findByPatientId(ivrSession.currentPatientId());

        Node nextNode = new Node();
        if (ivrInput.isNotSkipInput() && patient.isValidDose(ivrInput.input())) {
            nextNode = new ConfirmAdherenceNode(whpivrMessage).with(patient, parseInt(input)).node();
        } else {
            ivrSession.startOfAdherenceSubmission(now());
            addTransitionsToNextPatients(ivrSession, nextNode);
        }
        return nextNode.addOperations(new ResetPatientIndexOperation());
    }
}
