package org.motechproject.whp.webservice.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.mapping.StringToEnumeration;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isEmpty;
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
            SputumTrackingInstance smearSputumTrackingInstance = mapTestInstance(patientWebRequest.getSmear_sample_instance());
            LocalDate test1Date = stringToLocalDate(patientWebRequest.getSmear_test_date_1());
            LocalDate test2Date = stringToLocalDate(patientWebRequest.getSmear_test_date_2());
            SmearTestResult test1Result = mapTestResult(patientWebRequest.getSmear_test_result_1());
            SmearTestResult test2Result = mapTestResult(patientWebRequest.getSmear_test_result_2());
            String labName = patientWebRequest.getLab_name();
            String labNumber = patientWebRequest.getLab_number();
            smearTestResults.add(smearSputumTrackingInstance, test1Date, test1Result, test2Date, test2Result, labName, labNumber);
        }
        return smearTestResults;
    }

    private SputumTrackingInstance mapTestInstance(String instance) {
        return (SputumTrackingInstance) stringToEnumeration.convert(
                instance,
                SputumTrackingInstance.class
        );
    }

    private SmearTestResult mapTestResult(String result) {
        return null != result ? (SmearTestResult) stringToEnumeration.convert(result, SmearTestResult.class) : null;
    }

    private LocalDate stringToLocalDate(String string) {
        if (string != null){
            if (isEmpty(string)){
                return null;
            }
            return DateTime.parse(string, forPattern(DATE_FORMAT)).toLocalDate();
        }
        return null;

    }
}
