package org.motechproject.whp.importer.csv.mapper;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.mapping.StringToEnumeration;
import org.motechproject.whp.patient.domain.SmearTestResults;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.common.domain.WHPConstants;
import org.springframework.stereotype.Component;

import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.importer.csv.request.SmearTestResultRequests.SmearTestResultRequest;

@Component
public class ImportSmearTestResultsMapper {

    StringToEnumeration stringToEnumeration;

    public ImportSmearTestResultsMapper() {
        this.stringToEnumeration = new StringToEnumeration();
    }

    public SmearTestResults map(ImportPatientRequest importPatientRequest) {
        SmearTestResults smearTestResults = new SmearTestResults();
        for (SampleInstance instance : SampleInstance.values()) {
            SmearTestResultRequest request = importPatientRequest.getSmearTestResultRequestByType(instance);
            if (request != null && request.isNotEmpty()) {
                SmearTestResult test1Result = (SmearTestResult) stringToEnumeration.convert(request.getResult1(), SmearTestResult.class);
                LocalDate test1Date = stringToLocalDate(request.getDate1());
                SmearTestResult test2Result = (SmearTestResult) stringToEnumeration.convert(request.getResult2(), SmearTestResult.class);
                LocalDate test2Date = stringToLocalDate(request.getDate2());
                smearTestResults.add(instance, test1Date, test1Result, test2Date, test2Result);
            }
        }
        return smearTestResults;
    }

    private LocalDate stringToLocalDate(String string) {
        return DateTime.parse(string, forPattern(WHPConstants.DATE_FORMAT)).toLocalDate();
    }
}
