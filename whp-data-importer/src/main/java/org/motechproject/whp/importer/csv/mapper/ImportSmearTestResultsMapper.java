package org.motechproject.whp.importer.csv.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.importer.csv.request.SmearTestResultRequests.SmearTestResultRequest;
import static org.motechproject.whp.refdata.domain.SmearTestResult.valueOf;
import static org.motechproject.whp.refdata.domain.SmearTestSampleInstance.PreTreatment;

@Component
public class ImportSmearTestResultsMapper {

    public SmearTestResults map(ImportPatientRequest importPatientRequest) {
        SmearTestResults smearTestResults = new SmearTestResults();
        List<SmearTestResultRequest> requests = importPatientRequest.getAllSmearTestResults();
        for (SmearTestResultRequest request : requests) {
            //TODO: Add instance
            if (request.getDate1() != null) {
                SmearTestResult test1Result = valueOf(request.getResult1());
                LocalDate test1Date = stringToLocalDate(request.getDate1());
                SmearTestResult test2Result = valueOf(request.getResult2());
                LocalDate test2Date = stringToLocalDate(request.getDate2());
                smearTestResults.add(PreTreatment, test1Date, test1Result, test2Date, test2Result);
            }
        }
        return smearTestResults;
    }

    private LocalDate stringToLocalDate(String string) {
        return DateTime.parse(string, forPattern(WHPConstants.DATE_FORMAT)).toLocalDate();
    }
}
