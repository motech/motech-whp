package org.motechproject.whp.container.domain;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.domain.SputumTrackingInstance;

import static org.motechproject.whp.common.domain.Diagnosis.Pending;
import static org.motechproject.whp.common.domain.Diagnosis.Positive;
import static org.motechproject.whp.container.WHPContainerConstants.CLOSURE_DUE_TO_MAPPING;

@Data
@TypeDiscriminator("doc.type == 'Container'")
public class Container extends MotechBaseDataObject {

    private String containerId;

    private RegistrationInstance instance;

    private SputumTrackingInstance mappingInstance;

    private RegistrationInstance currentTrackingInstance;

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

    private DateTime closureDate;

    // Required for ektorp
    public Container() {
    }

    public Container(String providerId, String containerId, RegistrationInstance instance, DateTime creationTime, String district) {
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

    public void setClosureDate(DateTime closureDate) {
        this.closureDate = DateUtil.setTimeZone(closureDate);
    }

    public void mapWith(String patientId, String tbId, SputumTrackingInstance mappingInstance, ReasonForContainerClosure closureReasonForMapping, LocalDate consultationDate, DateTime closureDate) {
        setPatientId(patientId);
        setTbId(tbId);
        setDiagnosis(Positive);
        setMappingInstance(mappingInstance);
        setStatus(ContainerStatus.Closed);
        updateCurrentTrackingStatus();
        setReasonForClosure(closureReasonForMapping.getCode());
        setClosureDate(closureDate);
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
        setClosureDate(null);
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
        if(RegistrationInstance.getTrackingInstanceType(mappingInstance) == RegistrationInstance.PreTreatment)
            setConsultationDate(consultationDate);
        else
            setConsultationDate(null);
    }

    private void updateCurrentTrackingStatus() {
        currentTrackingInstance = mappingInstance == null ? instance : RegistrationInstance.getTrackingInstanceType(mappingInstance);
    }

    @JsonIgnore
    public boolean isClosedDueToMapping() {
        return reasonForClosure.equals(CLOSURE_DUE_TO_MAPPING);
    }
}
