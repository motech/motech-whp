package org.motechproject.whp.ivr.util;

import org.motechproject.decisiontree.FlowSession;
import org.motechproject.decisiontree.model.Node;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class FlowSessionStub implements FlowSession {

    private Map<String, Object> sessionAttributes = new HashMap<>();

    @Override
    public String getSessionId() {
        return "test-session-id";
    }

    @Override
    public String getLanguage() {
        return "en";
    }

    @Override
    public void setLanguage(String s) {
    }

    public <T extends Serializable> void set(String key, T value) {
        sessionAttributes.put(key, value);
    }

    public <T extends Serializable> T get(String key) {
        return (T) sessionAttributes.get(key);
    }

    @Override
    public void setCurrentNode(Node node) {
    }

    @Override
    public Node getCurrentNode() {
        return null;
    }
}
