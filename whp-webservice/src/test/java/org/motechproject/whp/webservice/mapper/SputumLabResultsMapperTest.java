package org.motechproject.whp.webservice.mapper;

import org.junit.Test;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.webservice.builder.SputumLabResultsWebRequestBuilder;
import org.motechproject.whp.webservice.request.SputumLabResultsWebRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

public class SputumLabResultsMapperTest {

    private SputumLabResultsMapper mapper = new SputumLabResultsMapper();

    @Test
    public void shouldMapSputumLabResultsToContainer() {
        String containerId = "containerId";
        SputumLabResultsWebRequest request = new SputumLabResultsWebRequestBuilder().withCase_id(containerId)
                .withDate_modified("03/04/2012 11:23:40")
                .withSmear_test_date_1("01/03/2012")
                .withSmear_test_result_1("Positive")
                .withSmear_test_date_2("01/03/2012")
                .withSmear_test_result_2("Positive")
                .withLab_name("XYZ")
                .withLab_number("1234")
                .build();

        Container container = new Container();
        container.setContainerId(containerId);

        mapper.map(request, container);

        LabResults labResults = container.getLabResults();

        assertThat(labResults.getSmearTestDate1(), is(parse(request.getSmear_test_date_1(), forPattern(DATE_FORMAT))));
        assertThat(labResults.getSmearTestDate2(), is(parse(request.getSmear_test_date_2(), forPattern(DATE_FORMAT))));
        assertThat(labResults.getSmearTestResult1(), is(request.getSmear_test_result_1()));
        assertThat(labResults.getSmearTestResult2(), is(request.getSmear_test_result_2()));
        assertThat(labResults.getLabName(), is(request.getLab_name()));
        assertThat(labResults.getLabNumber(), is(request.getLab_number()));
    }
}
