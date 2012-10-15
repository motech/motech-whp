package org.motechproject.whp.wgninbound.request;

import lombok.Data;
import org.motechproject.validation.constraints.NotNullOrEmpty;

import javax.validation.constraints.Size;

@Data
public class ProviderVerificationRequest {

    @NotNullOrEmpty
    @Size(min = 10, message = "should be atleast 10 dijits in length")
    private String msisdn;
    @NotNullOrEmpty
    private String call_id;
    private String time;

    /*Required for spring mvc*/
    public ProviderVerificationRequest() {
    }

    public ProviderVerificationRequest(String msisdn, String time, String call_id) {
        this.msisdn = msisdn;
        this.time = time;
        this.call_id = call_id;
    }

    public String getPhoneNumber() {
        int length = msisdn.length();
        return msisdn.substring(length - 10, length);
    }
}
