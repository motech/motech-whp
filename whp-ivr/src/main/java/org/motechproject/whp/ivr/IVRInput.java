package org.motechproject.whp.ivr;

import org.apache.commons.lang.StringUtils;

public class IVRInput {

    public static final String SKIP_PATIENT_CODE = "9";
    public static final String NO_INPUT_CODE = "";
    private String key;

    public IVRInput(String key) {
        this.key = key;
    }

    public boolean isSkipInput() {
        return SKIP_PATIENT_CODE.equals(key);
    }

    public boolean isNumeric() {
        return StringUtils.isNotEmpty(key) && StringUtils.isNumeric(key);
    }

    public boolean isNotSkipInput() {
        return !isSkipInput();
    }
    public boolean noInput() {
        return key.equals(NO_INPUT_CODE);
    }

    public String input() {
        return key;
    }

}
