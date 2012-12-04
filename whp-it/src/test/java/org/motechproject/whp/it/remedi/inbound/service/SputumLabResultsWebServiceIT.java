package org.motechproject.whp.it.remedi.inbound.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kubek2k.springockito.annotations.ReplaceWithMock;
import org.kubek2k.springockito.annotations.SpringockitoContextLoader;
import org.motechproject.http.client.service.HttpClientService;
import org.motechproject.whp.common.domain.RegistrationInstance;
import org.motechproject.whp.common.util.SpringIntegrationTest;
import org.motechproject.whp.container.builder.request.SputumLabResultsCaptureReportingRequestBuilder;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.repository.AllContainers;
import org.motechproject.whp.reporting.ReportingEventURLs;
import org.motechproject.whp.reports.contract.SputumLabResultsCaptureReportingRequest;
import org.motechproject.whp.webservice.service.SputumLabResultsWebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

@ContextConfiguration(loader = SpringockitoContextLoader.class, locations = "classpath*:/applicationITContext.xml")
public class SputumLabResultsWebServiceIT extends SpringIntegrationTest {

    @Autowired
    SputumLabResultsWebService sputumLabResultsWebService;
    @Autowired
    private ReportingEventURLs reportingEventURLs;
    @Autowired
    AllContainers allContainers;
    private Container container;

    @ReplaceWithMock
    @Autowired
    private HttpClientService httpClientService;

    @Before
    public void setUp() {
        reset(httpClientService);
        this.container = new Container();
        container.setContainerId("12345");
        container.setProviderId("providerId");
        container.setInstance(RegistrationInstance.InTreatment);

        allContainers.add(container);
        markForDeletion(container);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(httpClientService);
    }

    @Test
    public void shouldUpdateContainerLabResults() throws Exception {
        String requestBody = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"12345\" date_modified=\"03/04/2012\n" +
                "11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type>lab_results</update_type>\n" +
                "<smear_test_date_1>01/03/2012</smear_test_date_1>\n" +
                "<smear_test_result_1>Positive</smear_test_result_1>\n" +
                "<smear_test_date_2>01/03/2012</smear_test_date_2>\n" +
                "<smear_test_result_2>Positive</smear_test_result_2>\n" +
                "<lab_name>XYZ</lab_name>\n" +
                "<lab_number>1234</lab_number>\n" +
                "</update>\n" +
                "</case>";


        standaloneSetup(sputumLabResultsWebService).build()
                .perform(post("/sputumLabResults/process").body(requestBody.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isOk());

        Container container = allContainers.findByContainerId("12345");

        assertThat(container.getLabResults().getSmearTestDate1().toString(DATE_FORMAT), is("01/03/2012"));
        assertThat(container.getLabResults().getSmearTestDate2().toString(DATE_FORMAT), is("01/03/2012"));
        assertThat(container.getLabResults().getSmearTestResult1().value(), is("Positive"));
        assertThat(container.getLabResults().getSmearTestResult2().value(), is("Positive"));
        assertThat(container.getLabResults().getCumulativeResult().value(), is("Positive"));
        assertThat(container.getLabResults().getLabName(), is("XYZ"));
        assertThat(container.getLabResults().getLabNumber(), is("1234"));

        SputumLabResultsCaptureReportingRequest expectedReportingRequest = new SputumLabResultsCaptureReportingRequestBuilder().forContainer(container).build();

        verify(httpClientService).post(reportingEventURLs.getSputumLabResultsCaptureLogURL(), expectedReportingRequest);
    }

    @Test
    public void shouldReturnErrorResponseOnValidationError_forIncompleteSputumLabResult() throws Exception {
        String requestBodyWithValidationErrors = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"12345\" date_modified=\"03/04/2012\n" +
                "11:23:40\" user_id=\"system\" api_key=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                "<update>\n" +
                "<update_type></update_type>\n" +
                "<smear_test_date_1>01/03/2012</smear_test_date_1>\n" +
                "<smear_test_result_1>Positive</smear_test_result_1>\n" +
                "<smear_test_date_2>01/03/2012</smear_test_date_2>\n" +
                "<smear_test_result_2></smear_test_result_2>\n" +
                "<lab_name>XYZ</lab_name>\n" +
                "<lab_number>1234</lab_number>\n" +
                "</update>\n" +
                "</case>";

        standaloneSetup(sputumLabResultsWebService).build()
                .perform(post("/sputumLabResults/process").body(requestBodyWithValidationErrors.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(allOf(containsString("<message nature=\"submit_error\">Lab results are incomplete</message>"), containsString("<message>Lab results are incomplete</message>"))));

    }

    @Test
    public void shouldReturnErrorResponseOnValidationError_forInvalidApiKey() throws Exception {
        String requestBodyWithValidationErrors = "<?xml version=\"1.0\"?>\n" +
                "<case xmlns=\"http://openrosa.org/javarosa\" case_id=\"12345\" date_modified=\"03/04/2012\n" +
                "11:23:40\" user_id=\"system\" api_key=\"\">\n" +
                "<update>\n" +
                "<update_type>lab_results</update_type>\n" +
                "<smear_test_date_1>01/03/2012</smear_test_date_1>\n" +
                "<smear_test_result_1>Positive</smear_test_result_1>\n" +
                "<smear_test_date_2>01/03/2012</smear_test_date_2>\n" +
                "<smear_test_result_2>Positive</smear_test_result_2>\n" +
                "<lab_name>XYZ</lab_name>\n" +
                "<lab_number>1234</lab_number>\n" +
                "</update>\n" +
                "</case>";

        standaloneSetup(sputumLabResultsWebService).build()
                .perform(post("/sputumLabResults/process").body(requestBodyWithValidationErrors.getBytes()).contentType(MediaType.APPLICATION_XML))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("<message nature=\"submit_error\">field:api_key:api_key:is invalid.</message>")));

    }
}