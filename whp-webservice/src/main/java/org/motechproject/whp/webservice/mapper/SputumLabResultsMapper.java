package org.motechproject.whp.webservice.mapper;

import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.webservice.request.SputumLabResultsWebRequest;
import org.springframework.stereotype.Component;

import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

@Component
public class SputumLabResultsMapper {

    public void map(SputumLabResultsWebRequest request, Container container) {

        LabResults labResults = new LabResults();
        labResults.setSmearTestDate1(parse(request.getSmear_test_date_1(), forPattern(DATE_FORMAT)));
        labResults.setSmearTestResult1(request.getSmear_test_result_1());

        labResults.setSmearTestDate2(parse(request.getSmear_test_date_2(), forPattern(DATE_FORMAT)));
        labResults.setSmearTestResult2(request.getSmear_test_result_2());

        labResults.setLabName(request.getLab_name());
        labResults.setLabNumber(request.getLab_number());

        container.setLabResults(labResults);
    }
}
