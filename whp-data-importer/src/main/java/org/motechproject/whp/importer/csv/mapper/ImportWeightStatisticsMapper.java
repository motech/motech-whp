package org.motechproject.whp.importer.csv.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.springframework.stereotype.Component;

import static java.lang.Double.parseDouble;
import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.common.util.WHPDate.DATE_FORMAT;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;
import static org.motechproject.whp.importer.csv.request.WeightStatisticsRequests.WeightStatisticsRequest;
import static org.springframework.util.StringUtils.hasText;

@Component
public class ImportWeightStatisticsMapper {

    public WeightStatistics map(ImportPatientRequest importPatientRequest) {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (SputumTrackingInstance instance : SputumTrackingInstance.values()) {
            WeightStatisticsRequest request = importPatientRequest.getWeightStatisticsRequestByType(instance);
            if (request != null && request.isNotEmpty()) {
                LocalDate date = resolveMeasuringDate(importPatientRequest, request);
                weightStatistics.add(instance, parseDouble(request.getWeight()), date);
            }
        }
        return weightStatistics;
    }

    //TODO: Business logic in mapper - To be fixed by moving to Domain Object(s)
    private LocalDate resolveMeasuringDate(ImportPatientRequest importPatientRequest, WeightStatisticsRequest request) {
        if (hasText(request.getWeightDate())) {
            return parse(request.getWeightDate(), forPattern(DATE_FORMAT));
        } else {
            return parse(importPatientRequest.getDate_modified(), forPattern(DATE_TIME_FORMAT));
        }
    }
}
