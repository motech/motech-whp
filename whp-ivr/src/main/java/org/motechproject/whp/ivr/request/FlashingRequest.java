package org.motechproject.whp.ivr.request;

import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

@Data
public class FlashingRequest {
    private String mobileNumber;
    private DateTime callTime;

    public FlashingRequest() {
    }

    public FlashingRequest(String mobileNumber, DateTime callTime) {
        this.mobileNumber = mobileNumber;
        this.callTime = callTime;
    }

    public void setMobileNumber(String mobileNumber){
        this.mobileNumber = StringUtils.substring(mobileNumber, -10);
    }
}
