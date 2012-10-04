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

    private SputumTrackingInstance mappingInstance;

    private SputumTrackingInstance currentTrackingInstance;

    private String providerId;

    private LabResults labResults;

    private DateTime creationTime;

    private ContainerStatus status;

    private String patientId;

    private String tbId;

    // Required for ektorp
    public Container() {
        this.status = ContainerStatus.Open;
    }

    public Container(String providerId, String containerId, SputumTrackingInstance instance, DateTime creationTime) {
        this.providerId = providerId;
        this.containerId = containerId;
        this.instance = instance;
        this.currentTrackingInstance = this.instance;
        this.creationTime = creationTime;
        this.status = ContainerStatus.Open;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = DateUtil.setTimeZone(creationTime);
    }

    public void mapWith(String patientId, String tbId, SputumTrackingInstance mappingInstance) {
        setPatientId(patientId.toLowerCase());
        setTbId(tbId);
        setMappingInstance(mappingInstance);
        setStatus(ContainerStatus.Closed);
        updateCurrentTrackingStatus();
    }

    public void unMap() {
        setPatientId(null);
        setTbId(null);
        setMappingInstance(null);
        updateCurrentTrackingStatus();
        setStatus(ContainerStatus.Open);
    }

    private void updateCurrentTrackingStatus() {
        currentTrackingInstance = mappingInstance == null ? instance : SputumTrackingInstance.getTrackingInstanceType(mappingInstance);
    }
}
