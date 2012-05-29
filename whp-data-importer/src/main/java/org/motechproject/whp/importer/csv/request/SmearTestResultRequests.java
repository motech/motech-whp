package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.whp.refdata.domain.SmearTestResult;
import org.motechproject.whp.refdata.domain.SmearTestSampleInstance;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class SmearTestResultRequests {

    @Valid
    private SmearTestResultRequest preTreatmentSmearTestResult = new SmearTestResultRequest();
    private SmearTestResultRequest endIpSmearTestResult = new SmearTestResultRequest();
    private SmearTestResultRequest extendedIpSmearTestResult = new SmearTestResultRequest();
    private SmearTestResultRequest twoMonthsIntoCpSmearTestResult = new SmearTestResultRequest();
    private SmearTestResultRequest endTreatmentSmearTestResult = new SmearTestResultRequest();

    public void setDate1(SmearTestSampleInstance smearTestSampleInstance, String date1) {
        getSmearTestInstanceRecord(smearTestSampleInstance).setDate1(date1);
    }

    public void setResult1(SmearTestSampleInstance smearTestSampleInstance, String result1) {
        getSmearTestInstanceRecord(smearTestSampleInstance).setResult1(result1);
    }

    public void setDate2(SmearTestSampleInstance smearTestSampleInstance, String date2) {
        getSmearTestInstanceRecord(smearTestSampleInstance).setDate2(date2);
    }

    public void setResult2(SmearTestSampleInstance smearTestSampleInstance, String result2) {
        getSmearTestInstanceRecord(smearTestSampleInstance).setResult2(result2);
    }

    public SmearTestResultRequest getSmearTestInstanceRecord(SmearTestSampleInstance smearTestSampleInstance) {
        switch (smearTestSampleInstance) {
            case PreTreatment:
                return preTreatmentSmearTestResult;
            case EndIP:
                return endIpSmearTestResult;
            case ExtendedIP:
                return extendedIpSmearTestResult;
            case TwoMonthsIntoCP:
                return twoMonthsIntoCpSmearTestResult;
            case EndTreatment:
                return endTreatmentSmearTestResult;
            default:
                return null;
        }
    }

    public boolean hasSmearTestInstanceRecord(SmearTestSampleInstance type) {
        return StringUtils.hasText(getSmearTestInstanceRecord(type).getDate1());
    }

    public String getTestDate1(SmearTestSampleInstance type) {
        return getSmearTestInstanceRecord(type).getDate1();
    }

    public String getTestResult1(SmearTestSampleInstance type) {
        return getSmearTestInstanceRecord(type).getResult1();
    }

    public String getTestDate2(SmearTestSampleInstance type) {
        return getSmearTestInstanceRecord(type).getDate2();
    }

    public String getTestResult2(SmearTestSampleInstance type) {
        return getSmearTestInstanceRecord(type).getResult2();
    }

    public List<SmearTestResultRequest> getAll() {
        return asList(preTreatmentSmearTestResult,
                endIpSmearTestResult,
                extendedIpSmearTestResult,
                twoMonthsIntoCpSmearTestResult,
                endTreatmentSmearTestResult
        );
    }


    @Data
    public static class SmearTestResultRequest {

        @NotNullOrEmpty
        @DateTimeFormat(pattern = WHPConstants.DATE_FORMAT)
        private String date1;

        @NotNullOrEmpty
        @Enumeration(type = SmearTestResult.class)
        private String result1;

        @NotNullOrEmpty
        @DateTimeFormat(pattern = WHPConstants.DATE_FORMAT)
        private String date2;

        @NotNullOrEmpty
        @Enumeration(type = SmearTestResult.class)
        private String result2;
    }
}
