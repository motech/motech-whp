package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.validation.constraints.ValidateIfNotEmpty;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.util.StringUtils.hasText;

@Data
public class WeightStatisticsRequests {

    @Valid
    private WeightStatisticsRequest preTreatmentWeightStatistics = new WeightStatisticsRequest();
    @Valid
    private WeightStatisticsRequest endIpWeightStatistics = new WeightStatisticsRequest();
    @Valid
    private WeightStatisticsRequest extendedIpWeightStatistics = new WeightStatisticsRequest();
    @Valid
    private WeightStatisticsRequest twoMonthsIntoCpWeightStatistics = new WeightStatisticsRequest();
    @Valid
    private WeightStatisticsRequest endTreatmentWeightStatistics = new WeightStatisticsRequest();

    public void setWeight(SampleInstance SampleInstance, String weight) {
        getWeightStatisticsRecord(SampleInstance).setWeight(weight);
    }

    public void setWeightDate(SampleInstance SampleInstance, String weightDate) {
        getWeightStatisticsRecord(SampleInstance).setWeightDate(weightDate);
    }

    public WeightStatisticsRequest getWeightStatisticsRecord(SampleInstance SampleInstance) {
        switch (SampleInstance) {
            case PreTreatment:
                return preTreatmentWeightStatistics;
            case EndIP:
                return endIpWeightStatistics;
            case ExtendedIP:
                return extendedIpWeightStatistics;
            case TwoMonthsIntoCP:
                return twoMonthsIntoCpWeightStatistics;
            case EndTreatment:
                return endTreatmentWeightStatistics;
            default:
                return null;
        }
    }

    public boolean hasWeightInstanceRecord(SampleInstance type) {
        return StringUtils.hasText(getWeightStatisticsRecord(type).getWeightDate());
    }

    public String getWeightDate(SampleInstance type) {
        return getWeightStatisticsRecord(type).getWeightDate();
    }

    public String getWeight(SampleInstance type) {
        return getWeightStatisticsRecord(type).getWeight();
    }

    public List<WeightStatisticsRequest> getAll() {
        return asList(preTreatmentWeightStatistics,
                endIpWeightStatistics,
                extendedIpWeightStatistics,
                twoMonthsIntoCpWeightStatistics,
                endTreatmentWeightStatistics
        );
    }

    @Data
    @ValidateIfNotEmpty
    public static class WeightStatisticsRequest {

        @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "Weight must be a real number")
        private String weight;

        private String weightDate;

        public boolean isNotEmpty() {
            return hasText(weight);
        }
    }
}
