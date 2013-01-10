package org.motechproject.whp.common.domain;


import static org.apache.commons.lang.StringUtils.*;

public class PhoneNumber {

    private String msisdn;
    private boolean allowShort;
    private boolean allowString;

    public PhoneNumber(String msisdn) {
        this(msisdn, false);
    }

    public PhoneNumber(String msisdn, boolean allowShort) {
        this(msisdn, allowShort, false);
    }

    public PhoneNumber(String msisdn, boolean allowShort, boolean allowString) {
        this.allowShort = allowShort;
        this.allowString = allowString;
        this.msisdn = msisdn(msisdn);
    }

    public String value() {
        return msisdn == null ? msisdn : subString(msisdn);
    }

    private String msisdn(String msisdn) {
        return isValidMsisdn(msisdn) ? msisdn : null;
    }

    private boolean isValidMsisdn(String msisdn) {
        return isNotBlank(msisdn) && hasRequiredNumberOfDigits(msisdn) && isNumeric(msisdn);
    }

    private boolean hasRequiredNumberOfDigits(String msisdn) {
        return allowShort || msisdn.length() >= 10;
    }

    private String subString(String msisdn) {
        if (length(msisdn) > 10) {
            return substring(msisdn, msisdn.length() - 10);
        } else {
            return msisdn;
        }
    }
}
