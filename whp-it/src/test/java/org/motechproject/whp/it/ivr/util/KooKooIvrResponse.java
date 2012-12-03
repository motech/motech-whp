package org.motechproject.whp.it.ivr.util;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "response")
public class KooKooIvrResponse {

    List<String> playAudio;

    String gotoUrl;

    String hangup;

    @XmlElement(name = "playaudio")
    public List<String> getPlayAudio() {
        return playAudio;
    }

    public void setPlayAudio(List<String> playAudio) {
        this.playAudio = playAudio;
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

}
