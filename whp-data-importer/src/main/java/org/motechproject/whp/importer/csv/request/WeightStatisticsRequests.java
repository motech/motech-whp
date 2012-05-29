package org.motechproject.whp.importer.csv.request;

import lombok.Data;
import org.motechproject.validation.constraints.DateTimeFormat;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.WeightInstance;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class WeightStatisticsRequests {

    @Valid
    private WeightStatisticsRequest preTreatmentWeightStatistics = new WeightStatisticsRequest();
    private WeightStatisticsRequest endIpWeightStatistics = new WeightStatisticsRequest();
    private WeightStatisticsRequest extendedIpWeightStatistics = new WeightStatisticsRequest();
    private WeightStatisticsRequest twoMonthsIntoCpWeightStatistics = new WeightStatisticsRequest();
    private WeightStatisticsRequest endTreatmentWeightStatistics = new WeightStatisticsRequest();

    public boolean hasWeightStatistics(WeightInstance weightInstance) {
        return StringUtils.hasText(getWeightStatisticsRecord(weightInstance).getWeightDate());
    }

    public void setWeight(WeightInstance weightInstance, String weight) {
        getWeightStatisticsRecord(weightInstance).setWeight(weight);
    }

    public void setWeightDate(WeightInstance weightInstance, String weightDate) {
        getWeightStatisticsRecord(weightInstance).setWeightDate(weightDate);
    }

    public WeightStatisticsRequest getWeightStatisticsRecord(WeightInstance weightInstance) {
        switch (weightInstance) {
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

    public boolean hasWeightInstanceRecord(WeightInstance type) {
        return StringUtils.hasText(getWeightStatisticsRecord(type).getWeightDate());
    }

    public String getWeightDate(WeightInstance type) {
        return getWeightStatisticsRecord(type).getWeightDate();
    }

    public String getWeight(WeightInstance type) {
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
    public static class WeightStatisticsRequest {

        @Digits(integer = Integer.MAX_VALUE, fraction = Integer.MAX_VALUE, message = "Weight must be a real number")
        private String weight;

        @DateTimeFormat(pattern = WHPConstants.DATE_FORMAT, validateEmptyString = false)
        private String weightDate;
    }
}
