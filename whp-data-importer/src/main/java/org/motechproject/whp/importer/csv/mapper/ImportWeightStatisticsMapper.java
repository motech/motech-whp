package org.motechproject.whp.importer.csv.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.motechproject.whp.refdata.domain.WeightInstance;
import org.springframework.stereotype.Component;

import static java.lang.Double.parseDouble;
import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.importer.csv.request.WeightStatisticsRequests.WeightStatisticsRequest;

@Component
public class ImportWeightStatisticsMapper {

    public WeightStatistics map(ImportPatientRequest importPatientRequest) {
        WeightStatistics weightStatistics = new WeightStatistics();
        for (WeightInstance instance : WeightInstance.values()) {
            WeightStatisticsRequest request = importPatientRequest.getWeightStatisticsRequestByType(instance);
            if (request != null && request.getWeightDate() != null) {
                LocalDate date = parse(request.getWeightDate(), forPattern(WHPConstants.DATE_FORMAT));
                double weight = parseDouble(request.getWeight());
                weightStatistics.add(instance, weight, date);
            }
        }
        return weightStatistics;
    }
}
