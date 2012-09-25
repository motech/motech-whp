package org.motechproject.whp.webservice.builder;


import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

public class ContainerPatientMappingWebRequestBuilder {

    private ContainerPatientMappingWebRequest mappingWebRequest = new ContainerPatientMappingWebRequest();

    public ContainerPatientMappingWebRequest build() {
        return mappingWebRequest;
    }

    public ContainerPatientMappingWebRequestBuilder withCaseId(String caseId) {
        mappingWebRequest.setCase_id(caseId);
        return this;
    }

    public ContainerPatientMappingWebRequestBuilder withDateModified(String dateModified) {
        mappingWebRequest.setDate_modified(dateModified);
        return this;
    }

    public ContainerPatientMappingWebRequestBuilder withPatientId(String patientId) {
        mappingWebRequest.setPatient_id(patientId);
        return this;
    }

    public ContainerPatientMappingWebRequestBuilder withTbId(String tbId){
        mappingWebRequest.setTb_id(tbId);
        return this;
    }

    public ContainerPatientMappingWebRequestBuilder withInstance(String instance){
        mappingWebRequest.setSmear_sample_instance(instance);
        return this;
    }
}
