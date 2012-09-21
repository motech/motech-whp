package org.motechproject.whp.container.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;

@Data
@TypeDiscriminator("doc.type == 'Container'")
public class Container extends MotechBaseDataObject {

    private String containerId;

    private SputumTrackingInstance instance;

    private String providerId;

    private LabResults labResults;

    private DateTime creationDate;

    // Required for ektorp
    public Container() {
    }

    public Container(String providerId, String containerId, SputumTrackingInstance instance, DateTime creationTime) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
        this.creationDate= creationTime;
    }


    public DateTime getCreationDate() {
        return creationDate;
    }
}
