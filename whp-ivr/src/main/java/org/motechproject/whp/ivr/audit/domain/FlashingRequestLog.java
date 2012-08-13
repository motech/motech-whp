package org.motechproject.whp.ivr.audit.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@Data
@TypeDiscriminator("doc.type == 'FlashingRequestLog'")
public class FlashingRequestLog extends MotechBaseDataObject {

    public FlashingRequestLog(){ }

    public FlashingRequestLog(String mobileNumber, DateTime callTime){
        this.mobileNumber = mobileNumber;
        this.callTime = callTime;
    }

    @JsonProperty
    private DateTime creationTime = DateUtil.now();
    @JsonProperty
    private String mobileNumber;
    @JsonProperty
    private DateTime callTime;
    @JsonProperty
    private String providerId;

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = DateUtil.setTimeZone(creationTime);
    }

    public void setCallTime(DateTime callTime) {
        this.callTime = DateUtil.setTimeZone(callTime);
    }
}
