package org.motechproject.whp.importer.csv.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.stereotype.Component;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.importer.csv.request.SmearTestResultRequests.SmearTestResultRequest;
import static org.motechproject.whp.refdata.domain.SmearTestResult.valueOf;
import static org.springframework.util.StringUtils.hasText;

@Component
public class ImportSmearTestResultsMapper {

    public SmearTestResults map(ImportPatientRequest importPatientRequest) {
        SmearTestResults smearTestResults = new SmearTestResults();
        for (SmearTestSampleInstance instance : SmearTestSampleInstance.values()) {
            SmearTestResultRequest request = importPatientRequest.getSmearTestResultRequestByType(instance);
            if (isValid(request)) {
                SmearTestResult test1Result = valueOf(request.getResult1());
                LocalDate test1Date = stringToLocalDate(request.getDate1());
                SmearTestResult test2Result = valueOf(request.getResult2());
                LocalDate test2Date = stringToLocalDate(request.getDate2());
                smearTestResults.add(instance, test1Date, test1Result, test2Date, test2Result);
            }
        }
        return smearTestResults;
    }

    //TODO: Business Rule in mapper - To be fixed by moving to Domain Object(s)
    private boolean isValid(SmearTestResultRequest request) {
        return request != null && bothTestDatesAvailable(request) && bothTestResultsAvailable(request);
    }

    private boolean bothTestResultsAvailable(SmearTestResultRequest request) {
        return  hasText(request.getResult1()) && hasText(request.getResult2());
    }

    private boolean bothTestDatesAvailable(SmearTestResultRequest request) {
        return hasText(request.getDate1()) && hasText(request.getDate2());
    }

    private LocalDate stringToLocalDate(String string) {
        return DateTime.parse(string, forPattern(WHPConstants.DATE_FORMAT)).toLocalDate();
    }
}
