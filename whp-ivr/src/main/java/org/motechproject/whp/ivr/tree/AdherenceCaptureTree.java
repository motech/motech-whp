package org.motechproject.whp.ivr.tree;

import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Tree;
import org.motechproject.decisiontree.repository.AllTrees;
import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.transition.AdherenceSummaryTransition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.ivr.WHPIVRMessage.MUSIC_ENTER;
import static org.motechproject.whp.ivr.WHPIVRMessage.WELCOME_MESSAGE;

@Component
public class AdherenceCaptureTree {

    private AllTrees allTrees;
    private AdherenceSummaryTransition adherenceSummaryTransition;

    private WHPIVRMessage whpivrMessage;

    @Autowired
    public AdherenceCaptureTree(AllTrees allTrees, AdherenceSummaryTransition adherenceSummaryTransition, WHPIVRMessage whpivrMessage) {
        this.allTrees = allTrees;
        this.adherenceSummaryTransition = adherenceSummaryTransition;
        this.whpivrMessage = whpivrMessage;
    }

    @Seed(priority = 0, version = "3.0")
    public void load() {
        Tree adherenceCapture = new Tree()
                .setName("adherenceCapture")
                .setRootNode(new Node()
                        .addPrompts(new PromptBuilder(whpivrMessage).wav(MUSIC_ENTER).wav(WELCOME_MESSAGE).build())
                        .addTransition(
                                "1", adherenceSummaryTransition
                        )
                );
        allTrees.addOrReplace(adherenceCapture);
    }
}
