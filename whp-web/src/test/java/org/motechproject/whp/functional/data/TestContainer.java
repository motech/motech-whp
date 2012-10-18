package org.motechproject.whp.functional.data;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class TestContainer {

    private String containerId;

    private String providerId;

    private String instance;

    private String district;

    private String status;

    private String labResult;

    private String reasonForClosure;

    private String diagnosis;

    private String consultationDate;

    private String containerIssuedDate;

    public TestContainer setContainerId(String containerId) {
        this.containerId = containerId;
        return this;
    }

    public TestContainer setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public TestContainer setProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    public TestContainer setDistrict(String district) {
        this.district = district;
        return this;
    }

    public TestContainer setStatus(String status) {
        this.status = status;
        return this;
    }

    public TestContainer setReasonForClosure(String reasonForClosure) {
        this.reasonForClosure = reasonForClosure;
        return this;
    }

    public TestContainer setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public TestContainer setLabResult(String labResult) {
        this.labResult = labResult;
        return this;
    }

    public TestContainer setConsultationDate(String consultationDate) {
        this.consultationDate = consultationDate;
        return this;
    }

    public TestContainer setContainerIssuedDate(String containerIssuedDate) {
        this.containerIssuedDate = containerIssuedDate;
        return this;
    }
}
