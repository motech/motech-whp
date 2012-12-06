package org.motechproject.whp.common.domain;

import static org.apache.commons.lang.StringUtils.isNumeric;

public class PhoneNumber {

    private String msisdn;

    public PhoneNumber(String msisdn) {
        this.msisdn = msisdn(msisdn);
    }

    public String value() {
        return msisdn == null ? msisdn : msisdn.substring(msisdn.length() - 10);
    }

    private String msisdn(String msisdn) {
        return isValidMsisdn(msisdn) ? msisdn : null;
    }

    private boolean isValidMsisdn(String msisdn) {
        return msisdn != null && msisdn.length() >= 10 && isNumeric(msisdn);
    }
}
