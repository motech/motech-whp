package org.motechproject.whp.webservice.it.service;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.common.exception.WHPErrorCode;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.repository.AllPatients;
import org.motechproject.whp.webservice.service.ContainerPatientMappingWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(locations = "classpath*:/applicationWebServiceContext.xml")
public class ContainerPatientMappingWebServiceIT extends SpringIntegrationTest {

    @Autowired
    ContainerPatientMappingWebService containerPatientMappingWebService;

    @Autowired
    AllPatients allPatients;

    @Autowired
    AllContainers allContainers;

    @Before
    public void setup() {
        Patient patient = new PatientBuilder().withDefaults().build();
        patient.setPatientId("cha01102001");
        patient.getCurrentTreatment().setTbId("cha01102001");
        allPatients.add(patient);

        Container container = new Container("providerId", "12651654165465", null, DateTime.now());
        LabResults labResults = new LabResults();
        container.setLabResults(labResults);
        allContainers.add(container);

        markForDeletion(patient, container);
    }

    @Test
    public void shouldReturnSuccessResponse_forXMLRequest() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"12651654165465\" date_modified=\"03/04/2012 11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>patient_mapping</update_type>\n" +
                "<patient_id>cha01102001</patient_id>\n" +
                "<tb_id>cha01102001</tb_id>\n" +
                "<smear_sample_instance>PreTreatment</smear_sample_instance>\n" +
                "</update>\n" +
                "</case>";

        standaloneSetup(containerPatientMappingWebService).build().perform(post("/containerPatientMapping/process").body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isOk());
    }

    @Test
    public void shouldReturnErrorCode_forMalformedXMLRequest() throws Exception {
        String request = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"\" date_modified=\"03/04/2012 11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>patient_mapping</update_type>\n" +
                "<patient_id>cha01102001</patient_id>\n" +
                "<tb_id>cha01102001</tb_id>\n" +
                "<smear_sample_instance>Pre-Treatment</smear_sample_instance>\n" +
                "</update>\n" +
                "</case>";

        standaloneSetup(containerPatientMappingWebService).build().perform(post("/containerPatientMapping/process").body(request.getBytes()).contentType(MediaType.APPLICATION_XML)).andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(containsString("<message nature=\"submit_error\">" + WHPErrorCode.CONTAINER_PATIENT_MAPPING_IS_INCOMPLETE.getMessage() + "</message>"))));
    }
}
