package org.motechproject.whp.ivr;


import org.motechproject.ivr.message.IVRMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class WHPIVRMessage implements IVRMessage {

    private Properties properties;
    public static final String END_OF_CALL = "endCall";
    public static final String WELCOME = "welcome";
    private static final String CONTENT_LOCATION_URL = "content.location.url";
    private static final String WAV = ".wav";

    public static final String NUMBER_OF_PATIENTS_HAVING_ADHERENCE_CAPTURED = "noOfPatientsWithCapturedAdherence";
    public static final String NUMBER_OF_PATIENTS_PENDING_ADHERENCE_CAPTURE = "noOfPatientsPendingAdherenceCapture";

    @Autowired
    public WHPIVRMessage(@Qualifier("ivrProperties") Properties properties) {
        this.properties = properties;
    }

    public String get(String key) {
        return (String) properties.get(key.toLowerCase());
    }

    @Override
    public String getText(String key) {
        String text = get(key);
        return text == null ? key : text;
    }

    @Override
    public String getWav(String key, String preferredLangCode) {
        String file = get(key) != null ? get(key) : key;
        return String.format("%s%s/%s%s", properties.get(CONTENT_LOCATION_URL), preferredLangCode, file, WAV);
    }
}
