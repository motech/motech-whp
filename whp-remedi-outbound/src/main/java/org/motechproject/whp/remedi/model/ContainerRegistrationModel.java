package org.motechproject.whp.remedi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

@Data
@EqualsAndHashCode
public class ContainerRegistrationModel {
    private String containerId;
    private String dateModified;
    private String caseType;
    private String providerId;

    public ContainerRegistrationModel(String containerId, String providerId, SputumTrackingInstance caseType, DateTime dateModified) {
        this.containerId = containerId;
        this.providerId = providerId;
        this.caseType = caseType.name();
        this.dateModified = dateModified.toString(WHPDate.DATE_TIME_FORMAT);
    }
}
