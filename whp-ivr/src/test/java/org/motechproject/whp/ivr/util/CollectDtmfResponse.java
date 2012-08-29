package org.motechproject.whp.ivr.util;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class CollectDtmfResponse {

    List<String> playAudio;

    @XmlElement(name = "playaudio")
    public List<String> getPlayAudio() {
        return playAudio;
    }

    public void setPlayAudio(List<String> playAudio) {
        this.playAudio = playAudio;
    }
}
