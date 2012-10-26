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

    private String district;

    private LabResults labResults;

    private DateTime creationTime;

    private ContainerStatus status;

    private String patientId;

    private String tbId;

    private String reasonForClosure;

    private Diagnosis diagnosis;

    private String alternateDiagnosis;

    private LocalDate consultationDate;

    private LocalDate containerIssuedDate;

    // Required for ektorp
    public Container() {
    }

    public Container(String providerId, String containerId, SputumTrackingInstance instance, DateTime creationTime, String district) {
        setProviderId(providerId);
        this.district = district;
        this.containerId = containerId;
        this.instance = instance;
        this.creationTime = creationTime;
        this.status = ContainerStatus.Open;
        this.diagnosis = Pending;
        this.containerIssuedDate = DateUtil.newDate(creationTime);
    }

    public void setCreationTime(DateTime creationTime) {
        this.creationTime = DateUtil.setTimeZone(creationTime);
    }

    public void mapWith(String patientId, String tbId, SputumTrackingInstance mappingInstance, ReasonForContainerClosure closureReasonForMapping, LocalDate consultationDate) {
        setPatientId(patientId);
        setTbId(tbId);
        setDiagnosis(Positive);
        setMappingInstance(mappingInstance);
        setStatus(ContainerStatus.Closed);
        updateCurrentTrackingStatus();
        setReasonForClosure(closureReasonForMapping.getCode());
        updateConsultationDate(consultationDate, mappingInstance);
    }

    public void unMap() {
        setPatientId(null);
        setTbId(null);
        setDiagnosis(Pending);
        setMappingInstance(null);
        updateCurrentTrackingStatus();
        setStatus(ContainerStatus.Open);
        setReasonForClosure(null);
        setConsultationDate(null);
    }

    public void setPatientId(String patientId) {
        if (patientId != null)
            this.patientId = patientId.toLowerCase();
        else
            this.patientId = null;
    }

    public void setProviderId(String providerId) {
        if (providerId != null)
            this.providerId = providerId.toLowerCase();
        else
            this.providerId = null;
    }

    private void updateConsultationDate(LocalDate consultationDate, SputumTrackingInstance mappingInstance) {
        if(SputumTrackingInstance.getTrackingInstanceType(mappingInstance) == SputumTrackingInstance.PreTreatment)
            setConsultationDate(consultationDate);
        else
            setConsultationDate(null);
    }

    private void updateCurrentTrackingStatus() {
        currentTrackingInstance = mappingInstance == null ? instance : SputumTrackingInstance.getTrackingInstanceType(mappingInstance);
    }
}
