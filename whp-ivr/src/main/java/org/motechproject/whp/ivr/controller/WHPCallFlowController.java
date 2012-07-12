package org.motechproject.whp.ivr.controller;

import org.motechproject.decisiontree.model.Tree;
import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.extensions.CallFlowController;
import org.springframework.stereotype.Controller;

@Controller
public class WHPCallFlowController implements CallFlowController{
    @Override
    public String urlFor(KooKooIVRContext kooKooIVRContext) {
        return ControllerURLs.ADHERENCE_URL;
    }

    @Override
    public String decisionTreeName(KooKooIVRContext kooKooIVRContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Tree getTree(String s, KooKooIVRContext kooKooIVRContext) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void treeComplete(String s, KooKooIVRContext kooKooIVRContext) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
