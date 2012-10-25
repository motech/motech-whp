package org.motechproject.whp.webservice.builder;


import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.webservice.request.ContainerPatientMappingWebRequest;

public class ContainerPatientMappingWebRequestBuilder {

    private ContainerPatientMappingWebRequest mappingWebRequest = new ContainerPatientMappingWebRequest();

    public ContainerPatientMappingWebRequest build() {
        return mappingWebRequest;
    }

    public ContainerPatientMappingWebRequestBuilder withDefaults() {
        withCaseId("caseId")
                .withDateModified("03/04/2012 11:23:40")
                .withInstance(SampleInstance.PreTreatment.name())
                .withTbId("tbId")
                .withPatientId("patientId");
        mappingWebRequest.setApi_key("3F2504E04F8911D39A0C0305E82C3301");
        mappingWebRequest.setUpdate_type("patient_mapping");
        return this;
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

    public ContainerPatientMappingWebRequestBuilder withTbId(String tbId) {
        mappingWebRequest.setTb_id(tbId);
        return this;
    }

    public ContainerPatientMappingWebRequestBuilder withInstance(String instance) {
        mappingWebRequest.setSmear_sample_instance(instance);
        return this;
    }

    public ContainerPatientMappingWebRequestBuilder withTbRegistrationDate(String tbRegistrationDate) {
        mappingWebRequest.setTb_registration_date(tbRegistrationDate);
        return this;
    }
}
