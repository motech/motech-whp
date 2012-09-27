package org.motechproject.whp.container.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.refdata.domain.ContainerStatus;
import org.motechproject.whp.refdata.domain.SputumTrackingInstance;

@Data
@TypeDiscriminator("doc.type == 'Container'")
public class Container extends MotechBaseDataObject {

    private String containerId;

    private SputumTrackingInstance instance;

    private String providerId;

    private LabResults labResults;

    private DateTime creationTime;

    private ContainerStatus status;

    private String patientId;

    // Required for ektorp
    public Container() {
        this.status = ContainerStatus.Open;
    }

    public Container(String providerId, String containerId, SputumTrackingInstance instance, DateTime creationTime) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
        this.creationTime = creationTime;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = DateUtil.setTimeZone(creationTime);
    }

    public void mapWith(String patientId) {
        setPatientId(patientId);
        setStatus(ContainerStatus.Closed);
    }

    public void unMap() {
        setPatientId(null);
        setStatus(ContainerStatus.Open);
    }
}
