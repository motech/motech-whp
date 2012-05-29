package org.motechproject.whp.importer.csv.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.importer.csv.request.ImportPatientRequest;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.refdata.domain.WHPConstants;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.Double.parseDouble;
import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.importer.csv.request.WeightStatisticsRequests.WeightStatisticsRequest;
import static org.motechproject.whp.refdata.domain.WeightInstance.PreTreatment;

@Component
public class ImportWeightStatisticsMapper {

    public WeightStatistics map(ImportPatientRequest importPatientRequest) {
        WeightStatistics weightStatistics = new WeightStatistics();
        List<WeightStatisticsRequest> requests = importPatientRequest.getAllWeightStatisticsRequests();
        for (WeightStatisticsRequest request : requests) {
            //TODO: Add instance
            if (request.getWeightDate() != null) {
                //TODO: Verify the date format for import
                LocalDate date = parse(request.getWeightDate(), forPattern(WHPConstants.DATE_FORMAT));
                double weight = parseDouble(request.getWeight());
                weightStatistics.add(PreTreatment, weight, date);
            }
        }
        return weightStatistics;
    }
}
