package org.motechproject.whp.patient.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;

@Data
@TypeDiscriminator("doc.type == 'Provider'")
public class Provider extends MotechBaseDataObject {

    private String primaryMobile;

    private String secondaryMobile;

    private String tertiaryMobile;

    private String providerId;

    private String district;

    private DateTime lastModifiedDate;

    // Required for ektorp
    public Provider() {
    }

    public Provider(String providerId, String primaryMobile, String district, DateTime lastModifiedDate) {
        setProviderId(providerId);
        this.primaryMobile = primaryMobile;
        this.district = district;
        this.lastModifiedDate = lastModifiedDate;
    }

    public DateTime getLastModifiedDate() {
        return DateUtil.setTimeZone(lastModifiedDate);
    }

    public void setProviderId(String providerId) {
        if (providerId == null)
            this.providerId = null;
        else
            this.providerId = providerId.toLowerCase();
    }
}
