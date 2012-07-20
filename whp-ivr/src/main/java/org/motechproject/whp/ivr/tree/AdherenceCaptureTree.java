package org.motechproject.whp.ivr.tree;

import org.motechproject.decisiontree.model.Node;
import org.motechproject.decisiontree.model.Tree;
import org.motechproject.decisiontree.repository.AllTrees;
import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.adherence.service.WHPAdherenceService;
import org.motechproject.whp.ivr.WHPIVRMessage;
import org.motechproject.whp.ivr.builder.PromptBuilder;
import org.motechproject.whp.ivr.transition.ListPatientsForProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.whp.ivr.WHPIVRMessage.MUSIC_ENTER;
import static org.motechproject.whp.ivr.WHPIVRMessage.WELCOME_MESSAGE;

@Component
public class AdherenceCaptureTree {

    private AllTrees allTrees;
    private WHPAdherenceService whpAdherenceService;
    private ListPatientsForProvider listPatientsForProvider;

    private WHPIVRMessage whpivrMessage;

    @Autowired
    public AdherenceCaptureTree(AllTrees allTrees, WHPAdherenceService whpAdherenceService, ListPatientsForProvider listPatientsForProvider, WHPIVRMessage whpivrMessage) {
        this.allTrees = allTrees;
        this.whpAdherenceService = whpAdherenceService;
        this.listPatientsForProvider = listPatientsForProvider;
        this.whpivrMessage = whpivrMessage;
    }

    @Seed(priority = 0, version = "1.0")
    public void load() {
        Tree adherenceCapture = new Tree()
                .setName("adherenceCapture")
                .setRootNode(new Node()
                        .addPrompts(new PromptBuilder(whpivrMessage).wav(MUSIC_ENTER).wav(WELCOME_MESSAGE).build())
                        .addTransition(
                                "1", listPatientsForProvider
                        )
                );
        allTrees.addOrReplace(adherenceCapture);
    }
}
