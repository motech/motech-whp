package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.validation.constraints.Enumeration;
import org.motechproject.validation.constraints.NotNullOrEmpty;
import org.motechproject.validation.constraints.ValidateIfNotEmpty;
import org.motechproject.whp.common.domain.SampleInstance;
import org.motechproject.whp.common.domain.SmearTestResult;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import java.util.List;

import static java.util.Arrays.asList;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.springframework.util.StringUtils.hasText;

@Data
public class SmearTestResultRequests {

    @Valid
    private SmearTestResultRequest preTreatmentSmearTestResult = new SmearTestResultRequest();
    @Valid
    private SmearTestResultRequest endIpSmearTestResult = new SmearTestResultRequest();
    @Valid
    private SmearTestResultRequest extendedIpSmearTestResult = new SmearTestResultRequest();
    @Valid
    private SmearTestResultRequest twoMonthsIntoCpSmearTestResult = new SmearTestResultRequest();
    @Valid
    private SmearTestResultRequest endTreatmentSmearTestResult = new SmearTestResultRequest();

    public void setDate1(SampleInstance sampleInstance, String date1) {
        getSmearTestInstanceRecord(sampleInstance).setDate1(date1);
    }

    public void setResult1(SampleInstance sampleInstance, String result1) {
        getSmearTestInstanceRecord(sampleInstance).setResult1(result1);
    }

    public void setDate2(SampleInstance sampleInstance, String date2) {
        getSmearTestInstanceRecord(sampleInstance).setDate2(date2);
    }

    public void setResult2(SampleInstance sampleInstance, String result2) {
        getSmearTestInstanceRecord(sampleInstance).setResult2(result2);
    }

    public SmearTestResultRequest getSmearTestInstanceRecord(SampleInstance sampleInstance) {
        switch (sampleInstance) {
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

    public boolean hasSmearTestInstanceRecord(SampleInstance type) {
        return StringUtils.hasText(getSmearTestInstanceRecord(type).getDate1());
    }

    public String getTestDate1(SampleInstance type) {
        return getSmearTestInstanceRecord(type).getDate1();
    }

    public String getTestResult1(SampleInstance type) {
        return getSmearTestInstanceRecord(type).getResult1();
    }

    public String getTestDate2(SampleInstance type) {
        return getSmearTestInstanceRecord(type).getDate2();
    }

    public String getTestResult2(SampleInstance type) {
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
    @ValidateIfNotEmpty
    public static class SmearTestResultRequest {

        @NotNullOrEmpty
        @DateTimeFormat(pattern = DATE_FORMAT)
        private String date1;

        @NotNullOrEmpty
        @Enumeration(type = SmearTestResult.class)
        private String result1;

        @NotNullOrEmpty
        @DateTimeFormat(pattern = DATE_FORMAT)
        private String date2;

        @NotNullOrEmpty
        @Enumeration(type = SmearTestResult.class)
        private String result2;

        public boolean isNotEmpty() {
            return hasText(date1);
        }
    }
}
