package org.motechproject.whp.webservice.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.common.domain.SputumTrackingInstance;
import org.motechproject.whp.common.util.WHPDateTime;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Component
public class WeightStatisticsMapper {

    public WeightStatistics map(PatientWebRequest patientWebRequest) {
        WeightStatistics statistics = new WeightStatistics();
        if (isNotEmpty(patientWebRequest.getWeight_instance())) {
            LocalDate measuringDate = WHPDateTime.date(patientWebRequest.getDate_modified()).dateTime().toLocalDate();
            statistics.add(
                    SputumTrackingInstance.valueOf(patientWebRequest.getWeight_instance()),
                    Double.valueOf(patientWebRequest.getWeight()),
                    measuringDate
            );
        }
        return statistics;
    }
}
