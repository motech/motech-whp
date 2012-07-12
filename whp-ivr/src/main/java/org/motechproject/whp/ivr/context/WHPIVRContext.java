package org.motechproject.whp.ivr.context;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.motechproject.ivr.kookoo.KooKooIVRContext;
import org.motechproject.ivr.kookoo.KookooRequest;
import org.motechproject.ivr.kookoo.eventlogging.CallEventConstants;
import org.motechproject.ivr.model.CallDirection;
import org.motechproject.whp.ivr.domain.CallState;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

public class WHPIVRContext {
    static final String CALLER_ID = "caller_id";
    public static final String PATIENT_ID = "patient_id";
    private static final String CALL_START_TIME = "call_time";

    protected KookooRequest kookooRequest;
    protected HttpServletRequest httpRequest;
    private KooKooIVRContext kooKooIVRContext;

    public WHPIVRContext(KooKooIVRContext kooKooIVRContext) {
        this(kooKooIVRContext.kooKooRequest(), kooKooIVRContext.httpRequest());
        this.kooKooIVRContext = kooKooIVRContext;
    }

    public WHPIVRContext(WHPIVRContext whpIvrContext) {
        this(whpIvrContext.kookooRequest, whpIvrContext.httpRequest);
        this.kooKooIVRContext = whpIvrContext.kooKooIVRContext;
    }

    WHPIVRContext(KookooRequest kookooRequest, HttpServletRequest httpRequest) {
        this.kookooRequest = kookooRequest;
        this.httpRequest = httpRequest;
    }

    public KooKooIVRContext getKooKooIVRContext() {
        return kooKooIVRContext;
    }

    public void initialize() {
        callerId(requestedCallerId());
    }

    private void setInSession(String name, Object value) {
        httpRequest.getSession().setAttribute(name, value);
    }

    public void addLastCompletedTreeToListOfCompletedTrees(String treeName) {
        kooKooIVRContext.addToListOfCompletedTrees(treeName);
    }

    public List<String> getListOfCompletedTrees() {
        return kooKooIVRContext.getListOfCompletedTrees();
    }

    public boolean hasTraversedTree(String treeName) {
        return getListOfCompletedTrees() != null && getListOfCompletedTrees().contains(treeName);
    }

    public boolean hasTraversedAnyTree() {
        return CollectionUtils.isNotEmpty(getListOfCompletedTrees());
    }

    protected void callerId(String callerId) {
        setInSession(CALLER_ID, callerId);
    }

    public String dtmfInput() {
        return kookooRequest.getInput();
    }

    public String callerId() {
        return fromSession(CALLER_ID);
    }

    private String fromSession(String name) {
        return (String) httpRequest.getSession().getAttribute(name);
    }

    public CallState callState() {
        String value = fromSession(CallEventConstants.CALL_STATE);
        return (value == null) ? CallState.STARTED : Enum.valueOf(CallState.class, value);
    }

    public String callId() {
        return kooKooIVRContext.callId();
    }

    public String callDetailRecordId() {
        return kooKooIVRContext.callDetailRecordId();
    }

    public CallDirection callDirection() {
        return kookooRequest.getCallDirection();
    }

    public boolean isIncomingCall() {
        return callDirection().equals(CallDirection.Inbound);
    }

    public boolean isOutgoingCall() {
        return callDirection().equals(CallDirection.Outbound);
    }

    public DateTime callStartTime() {
        return (DateTime) httpRequest.getSession().getAttribute(CALL_START_TIME);
    }

    public String patientDocumentId() {
        return fromSession(PATIENT_ID);
    }

    public void callState(CallState callState) {
        setInSession(CallEventConstants.CALL_STATE, callState.toString());
        log(CallEventConstants.CALL_STATE, callState.toString());
    }

    private void log(String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        setDataToLog(map);
    }

    public String requestedCallerId() {
        return kookooRequest.getCid();
    }

    public String preferredLanguage() {
        return kooKooIVRContext.preferredLanguage();
    }

    public void currentDecisionTreePath(String path) {
        kooKooIVRContext.currentDecisionTreePath(path);
    }

    public void setDataToLog(HashMap<String, String> map) {
        kooKooIVRContext.dataToLog(map);
    }

    public boolean isAnswered() {
        return kooKooIVRContext.isAnswered();
    }
}
