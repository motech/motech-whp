package org.motechproject.whp.ivr.tree;

import org.motechproject.decisiontree.model.Tree;
import org.motechproject.decisiontree.repository.AllTrees;
import org.motechproject.deliverytools.seed.Seed;
import org.motechproject.whp.ivr.transition.AdherenceSummaryTransition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdherenceCaptureTree {

    private AllTrees allTrees;
    private AdherenceSummaryTransition adherenceSummaryTransition;

    @Autowired
    public AdherenceCaptureTree(AllTrees allTrees, AdherenceSummaryTransition adherenceSummaryTransition) {
        this.allTrees = allTrees;
        this.adherenceSummaryTransition = adherenceSummaryTransition;
    }

    @Seed(priority = 0, version = "3.0")
    public void load() {
        Tree adherenceCapture = new Tree()
                .setName("adherenceCapture")
                .setRootTransition(adherenceSummaryTransition);
        allTrees.addOrReplace(adherenceCapture);
    }
}
