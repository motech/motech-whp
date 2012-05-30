package org.motechproject.whp.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.contract.PatientWebRequest;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.refdata.domain.SmearTestSampleInstance.valueOf;

@Component
public class SmearTestResultsMapper {

    public SmearTestResults map(PatientWebRequest patientWebRequest) {
        SmearTestResults smearTestResults = new SmearTestResults();
        if (isNotEmpty(patientWebRequest.getSmear_sample_instance())) {
            SmearTestSampleInstance smearSampleInstance = valueOf(patientWebRequest.getSmear_sample_instance());
            LocalDate test1Date = stringToLocalDate(patientWebRequest.getSmear_test_date_1());
            LocalDate test2Date = stringToLocalDate(patientWebRequest.getSmear_test_date_2());
            SmearTestResult test1Result = SmearTestResult.valueOf(patientWebRequest.getSmear_test_result_1());
            SmearTestResult test2Result = SmearTestResult.valueOf(patientWebRequest.getSmear_test_result_2());
            smearTestResults.add(smearSampleInstance, test1Date, test1Result, test2Date, test2Result);
        }
        return smearTestResults;
    }

    private LocalDate stringToLocalDate(String string) {
        return DateTime.parse(string, forPattern(WHPConstants.DATE_FORMAT)).toLocalDate();
    }
}
