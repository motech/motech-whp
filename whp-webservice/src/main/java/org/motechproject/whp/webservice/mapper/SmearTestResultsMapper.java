package org.motechproject.whp.webservice.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.mapping.StringToEnumeration;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;

@Component
public class SmearTestResultsMapper {

    StringToEnumeration stringToEnumeration;

    public SmearTestResultsMapper() {
        stringToEnumeration = new StringToEnumeration();
    }

    public SmearTestResults map(PatientWebRequest patientWebRequest) {
        SmearTestResults smearTestResults = new SmearTestResults();
        if (isNotEmpty(patientWebRequest.getSmear_sample_instance())) {
            SampleInstance smearSampleInstance = mapTestInstance(patientWebRequest.getSmear_sample_instance());
            LocalDate test1Date = stringToLocalDate(patientWebRequest.getSmear_test_date_1());
            LocalDate test2Date = stringToLocalDate(patientWebRequest.getSmear_test_date_2());
            SmearTestResult test1Result = mapTestResult(patientWebRequest.getSmear_test_result_1());
            SmearTestResult test2Result = mapTestResult(patientWebRequest.getSmear_test_result_2());
            smearTestResults.add(smearSampleInstance, test1Date, test1Result, test2Date, test2Result);
        }
        return smearTestResults;
    }

    private SampleInstance mapTestInstance(String instance) {
        return (SampleInstance) stringToEnumeration.convert(
                instance,
                SampleInstance.class
        );
    }

    private SmearTestResult mapTestResult(String result) {
        return (SmearTestResult) stringToEnumeration.convert(
                result,
                SmearTestResult.class
        );
    }

    private LocalDate stringToLocalDate(String string) {
        return DateTime.parse(string, forPattern(DATE_FORMAT)).toLocalDate();
    }
}
