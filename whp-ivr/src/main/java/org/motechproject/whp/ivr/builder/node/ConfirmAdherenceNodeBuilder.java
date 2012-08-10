package org.motechproject.whp.ivr.builder.node;

import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.ivr.WhpIvrMessage;
import org.motechproject.whp.ivr.operation.GetAdherenceOperation;
import org.motechproject.whp.ivr.transition.ConfirmAdherenceTransition;
import org.motechproject.whp.patient.domain.Patient;

import static org.motechproject.whp.ivr.prompts.ProvidedAdherencePrompts.providedAdherencePrompts;

public class ConfirmAdherenceNodeBuilder {

    private final Node node;
    private final WhpIvrMessage whpIvrMessage;

    public ConfirmAdherenceNodeBuilder(WhpIvrMessage whpIvrMessage) {
        node = new Node();
        this.whpIvrMessage = whpIvrMessage;
    }

    public ConfirmAdherenceNodeBuilder with(Patient patient, Integer adherenceInput) {
        if (patient.isValidDose(adherenceInput)) {
            node.addOperations(new GetAdherenceOperation());
            node.addPrompts(providedAdherencePrompts(whpIvrMessage, patient.getPatientId(), adherenceInput, patient.dosesPerWeek()));
            node.addTransition("?", new ConfirmAdherenceTransition());
        }
        return this;
    }

    public Node node() {
        return node;
    }
}
