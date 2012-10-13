package org.motechproject.whp.webservice.mapper;

import org.motechproject.whp.common.mapping.StringToEnumeration;
import org.motechproject.whp.container.domain.Container;
import org.motechproject.whp.container.domain.LabResults;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.webservice.request.SputumLabResultsWebRequest;
import org.springframework.stereotype.Component;

import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

@Component
public class SputumLabResultsMapper {

    private StringToEnumeration stringToEnumeration;

    public SputumLabResultsMapper() {
        this.stringToEnumeration = new StringToEnumeration();
    }

    public void map(SputumLabResultsWebRequest request, Container container) {
        LabResults labResults = new LabResults();
        labResults.setSmearTestDate1(parse(request.getSmear_test_date_1(), forPattern(DATE_FORMAT)));
        labResults.setSmearTestResult1(mapTestResult(request.getSmear_test_result_1()));

        labResults.setSmearTestDate2(parse(request.getSmear_test_date_2(), forPattern(DATE_FORMAT)));
        labResults.setSmearTestResult2(mapTestResult(request.getSmear_test_result_2()));

        labResults.setLabName(request.getLab_name());
        labResults.setLabNumber(request.getLab_number());

        labResults.updateCumulativeResult();
        container.setLabResults(labResults);
    }

    private SmearTestResult mapTestResult(String result) {
        return (SmearTestResult) stringToEnumeration.convert(
                result,
                SmearTestResult.class
        );
    }

}
