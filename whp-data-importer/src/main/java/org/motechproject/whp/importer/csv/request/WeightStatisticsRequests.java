package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.validation.constraints.ValidateIfNotEmpty;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
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

    public void setWeight(SputumTrackingInstance SputumTrackingInstance, String weight) {
        getWeightStatisticsRecord(SputumTrackingInstance).setWeight(weight);
    }

    public void setWeightDate(SputumTrackingInstance SputumTrackingInstance, String weightDate) {
        getWeightStatisticsRecord(SputumTrackingInstance).setWeightDate(weightDate);
    }

    public WeightStatisticsRequest getWeightStatisticsRecord(SputumTrackingInstance SputumTrackingInstance) {
        switch (SputumTrackingInstance) {
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

    public boolean hasWeightInstanceRecord(SputumTrackingInstance type) {
        return StringUtils.hasText(getWeightStatisticsRecord(type).getWeightDate());
    }

    public String getWeightDate(SputumTrackingInstance type) {
        return getWeightStatisticsRecord(type).getWeightDate();
    }

    public String getWeight(SputumTrackingInstance type) {
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
