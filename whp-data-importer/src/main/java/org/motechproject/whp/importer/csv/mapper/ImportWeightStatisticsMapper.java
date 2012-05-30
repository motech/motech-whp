package org.motechproject.whp.importer.csv.mapper;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.importer.csv.request.WeightStatisticsRequests;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.WeightInstance;
import org.springframework.stereotype.Component;

import static java.lang.Double.parseDouble;
import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.importer.csv.request.WeightStatisticsRequests.WeightStatisticsRequest;
import static org.springframework.util.StringUtils.hasText;

@Component
public class ImportWeightStatisticsMapper {

    public WeightStatistics map(ImportPatientRequest importPatientRequest) {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (WeightInstance instance : WeightInstance.values()) {
            WeightStatisticsRequest request = importPatientRequest.getWeightStatisticsRequestByType(instance);
            if (isValid(request)) {
                LocalDate date = resolveMeasuringDate(importPatientRequest, request);
                weightStatistics.add(instance, parseDouble(request.getWeight()), date);
            }
        }
        return weightStatistics;
    }

    //TODO: Business Rule in mapper - To be fixed by moving to Domain Object(s)
    private boolean isValid(WeightStatisticsRequest request) {
        return request != null && hasText(request.getWeight());
    }

    //TODO: Business logic in mapper - To be fixed by moving to Domain Object(s)
    private LocalDate resolveMeasuringDate(ImportPatientRequest importPatientRequest, WeightStatisticsRequest request) {
        if (hasText(request.getWeightDate())) {
            return parse(request.getWeightDate(), forPattern(WHPConstants.DATE_FORMAT));
        } else {
            return parse(importPatientRequest.getDate_modified(), forPattern(WHPConstants.DATE_TIME_FORMAT));
        }
    }
}
