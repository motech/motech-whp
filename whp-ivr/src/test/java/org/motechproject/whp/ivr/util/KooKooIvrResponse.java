package org.motechproject.whp.ivr.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "response")
public class KooKooIvrResponse {

    List<String> noticePrompts;

    CollectDtmfResponse collectDtmf;

    String gotoUrl;

    String hangup;



    @XmlElement(name = "playaudio")
    public List<String> getNoticePrompts() {
        return noticePrompts;
    }

    public void setNoticePrompts(List<String> noticePrompts) {
        this.noticePrompts = noticePrompts;
    }

    @XmlElement(name = "collectdtmf")
    public CollectDtmfResponse getCollectDtmf() {
        return collectDtmf;
    }

    public void setCollectDtmf(CollectDtmfResponse collectDtmf) {
        this.collectDtmf = collectDtmf;
    }

    @XmlElement(name = "gotourl")
    public String getGotoUrl() {
        return gotoUrl;
    }

    public void setGotoUrl(String gotoUrl) {
        this.gotoUrl = gotoUrl;
    }

    @XmlElement(name = "hangup")
    public String getHangup() {
        return hangup;
    }

    public void setHangup(String hangup) {
        this.hangup = hangup;
    }

    public boolean callEnded() {
        return hangup != null && gotoUrl == null;
    }

    public List<String> getTransitionPrompts() {
        return collectDtmf.getPlayAudio();
    }
}


