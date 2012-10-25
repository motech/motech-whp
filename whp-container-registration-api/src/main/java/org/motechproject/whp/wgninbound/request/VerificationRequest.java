package org.motechproject.whp.wgninbound.request;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class VerificationRequest {

    protected String msisdn;

    /*Required for spring mvc*/
    public VerificationRequest() {
    }

    public VerificationRequest(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPhoneNumber() {
        int length = msisdn.length();
        return msisdn.substring(length - 10, length);
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
}
