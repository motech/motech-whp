package org.motechproject.whp.container.builder;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.ContainerStatus;
import org.motechproject.whp.common.domain.Diagnosis;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;

public class ContainerBuilder {

    private Container container = new Container();

    public static ContainerBuilder newContainer() {
        return new ContainerBuilder();
    }

    public ContainerBuilder withDefaults() {
        withProviderId("providerId").withContainerId("containerId");
        return this;
    }

    public ContainerBuilder withProviderId(String providerId) {
        container.setProviderId(providerId);
        return this;
    }

    public ContainerBuilder withContainerId(String containerId) {
        container.setContainerId(containerId);
        return this;
    }

    public ContainerBuilder withPatientId(String patientId) {
        container.setPatientId(patientId);
        return this;
    }

    public ContainerBuilder withInstance(SputumTrackingInstance instance) {
        container.setInstance(instance);
        container.setCurrentTrackingInstance(instance);
        return this;
    }

    public Container build() {
        return container;
    }

    public ContainerBuilder withSmearTestResult1(SmearTestResult smearTestResult) {
        if(container.getLabResults() == null){
            container.setLabResults(new LabResults());
        }
        container.getLabResults().setSmearTestResult1(smearTestResult);
        return this;
    }

    public ContainerBuilder withSmearTestResult2(SmearTestResult smearTestResult) {
        if(container.getLabResults() == null){
            container.setLabResults(new LabResults());
        }
        container.getLabResults().setSmearTestResult2(smearTestResult);
        return this;
    }

    public ContainerBuilder withStatus(ContainerStatus status) {
        container.setStatus(status);
        return this;
    }

    public ContainerBuilder withContainerIssuedDate(LocalDate date) {
        container.setContainerIssuedDate(date);
        return this;
    }

    public ContainerBuilder withTbId(String tbId) {
        container.setTbId(tbId);
        return this;
    }

    public ContainerBuilder withDiagnosis(Diagnosis diagnosis) {
        container.setDiagnosis(diagnosis);
        return this;
    }

    public ContainerBuilder withCumulativeResult(SmearTestResult smearTestResult) {
        if(container.getLabResults() == null) {
            container.setLabResults(new LabResults());
        }
        container.getLabResults().setCumulativeResult(smearTestResult);
        return this;
    }

    public ContainerBuilder withReasonForClosure(String reasonForClosure) {
        container.setReasonForClosure(reasonForClosure);
        return this;
    }

    public ContainerBuilder withConsultationDate(LocalDate consultationDate) {
        container.setConsultationDate(consultationDate);
        return this;
    }

    public ContainerBuilder withProviderDistrict(String districtName) {
        container.setDistrict(districtName);
        return this;
    }
}
