package org.motechproject.whp.container.domain;

import lombok.Data;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;


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

    private String reasonForClosure;

    private Diagnosis diagnosis;

    private String alternateDiagnosis;

    private LocalDate consultationDate;

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
        this.diagnosis = Pending;
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = DateUtil.setTimeZone(creationTime);
    }

    public void mapWith(String patientId, String tbId, SputumTrackingInstance mappingInstance, ReasonForContainerClosure closureReasonForMapping) {
        setPatientId(patientId.toLowerCase());
        setTbId(tbId);
        setDiagnosis(Positive);
        setMappingInstance(mappingInstance);
        setStatus(ContainerStatus.Closed);
        updateCurrentTrackingStatus();
        setReasonForClosure(closureReasonForMapping.getCode());
    }

    public void unMap() {
        setPatientId(null);
        setTbId(null);
        setDiagnosis(Pending);
        setMappingInstance(null);
        updateCurrentTrackingStatus();
        setStatus(ContainerStatus.Open);
    }

    private void updateCurrentTrackingStatus() {
        currentTrackingInstance = mappingInstance == null ? instance : SputumTrackingInstance.getTrackingInstanceType(mappingInstance);
    }
}
