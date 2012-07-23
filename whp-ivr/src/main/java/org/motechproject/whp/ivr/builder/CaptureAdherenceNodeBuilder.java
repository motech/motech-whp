package org.motechproject.whp.ivr.builder;


import org.motechproject.decisiontree.model.Node;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.transition.AdherenceCapture;

import static org.motechproject.whp.ivr.IvrAudioFiles.*;

public class CaptureAdherenceNodeBuilder {

    Node node;
    PromptBuilder promptBuilder;

    public CaptureAdherenceNodeBuilder(WHPIVRMessage whpivrMessage) {
        node = new Node();
        promptBuilder = new PromptBuilder(whpivrMessage);
    }

    public CaptureAdherenceNodeBuilder patientSummary(int noOfPatientsWithAdherence, int noOfPatientsWithoutAdherence) {
        promptBuilder.wav(ADHERENCE_PROVIDED_FOR)
                .number(noOfPatientsWithAdherence)
                .wav(ADHERENCE_TO_BE_PROVIDED_FOR)
                .number(noOfPatientsWithoutAdherence)
                .wav(ADHERENCE_CAPTURE_INSTRUCTION);
        return this;
    }

    public CaptureAdherenceNodeBuilder captureAdherence(String patientId, int position) {
        promptBuilder.wav(PATIENT_LIST)
                .number(position)
                .id(patientId)
                .wav(ENTER_ADHERENCE);
        node.addTransition("?", new AdherenceCapture());
        return this;
    }

    public CaptureAdherenceNodeBuilder confirmAdherence(String patientId, int adherenceInput, int dosesPerWeek) {
        promptBuilder.wav(CONFIRM_ADHERENCE)
                .id(patientId)
                .wav(HAS_TAKEN)
                .number(adherenceInput)
                .wav(OUT_OF)
                .number(dosesPerWeek)
                .wav(DOSES);
        return this;
    }

    public Node build() {
        node.setPrompts(promptBuilder.build());
        return node;
    }


}

