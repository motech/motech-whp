package org.motechproject.whp.ivr;

import org.apache.commons.lang.StringUtils;

import static java.lang.Integer.parseInt;

public class IVRInput {

    private static final String SKIP_PATIENT_CODE = "9";
    private String key;

    public IVRInput(String key) {
        this.key = key;
    }

    public boolean isSkipInput() {
        return !StringUtils.isNumeric(key) || SKIP_PATIENT_CODE.equals(key);
    }

    public boolean isNotSkipInput() {
        return !isSkipInput();
    }

    public int input() {
        return parseInt(key);
    }
}
