package org.motechproject.whp.webservice.request;

import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

public class ContainerPatientMappingWebRequestTest {

    @Test
    public void shouldReturnFalseForUnMappingCase() {
        ContainerPatientMappingWebRequest request = new ContainerPatientMappingWebRequest();
        request.setApi_key("ApiKey");
        request.setCase_id("caseId");
        request.setDate_modified("Date");
        request.setUpdate_type("Update");
        request.setPatient_id("");
        request.setSmear_sample_instance("");
        request.setTb_id("");

        assertFalse(request.isMappingRequest());

    }

    @Test
    public void shouldReturnTrueForMappingCase() {

        ContainerPatientMappingWebRequest request = new ContainerPatientMappingWebRequest();
        request.setApi_key("ApiKey");
        request.setCase_id("caseId");
        request.setDate_modified("Date");
        request.setUpdate_type("Update");
        request.setPatient_id("Patient");
        request.setSmear_sample_instance("Instance");
        request.setTb_id("Tb");

        assertTrue(request.isMappingRequest());

        request = new ContainerPatientMappingWebRequest();
        request.setApi_key("ApiKey");
        request.setCase_id("caseId");
        request.setDate_modified("Date");
        request.setUpdate_type("Update");
        request.setPatient_id("");
        request.setSmear_sample_instance("Instance");
        request.setTb_id("Tb");

        assertTrue(request.isMappingRequest());

        request = new ContainerPatientMappingWebRequest();
        request.setApi_key("ApiKey");
        request.setCase_id("caseId");
        request.setDate_modified("Date");
        request.setUpdate_type("Update");
        request.setPatient_id("Patient");
        request.setSmear_sample_instance("");
        request.setTb_id("Tb");

        assertTrue(request.isMappingRequest());

        request = new ContainerPatientMappingWebRequest();
        request.setApi_key("ApiKey");
        request.setCase_id("caseId");
        request.setDate_modified("Date");
        request.setUpdate_type("Update");
        request.setPatient_id("Patient");
        request.setSmear_sample_instance("Instance");
        request.setTb_id("");

        assertTrue(request.isMappingRequest());
    }
}
