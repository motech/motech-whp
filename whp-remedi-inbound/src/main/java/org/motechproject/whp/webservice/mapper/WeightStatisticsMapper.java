package org.motechproject.whp.webservice.mapper;

import org.joda.time.LocalDate;
import org.motechproject.whp.patient.domain.WeightStatistics;
import org.motechproject.whp.refdata.domain.SampleInstance;
import org.motechproject.whp.webservice.request.PatientWebRequest;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang.StringUtils.isNotEmpty;
import static org.joda.time.LocalDate.parse;
import static org.joda.time.format.DateTimeFormat.forPattern;
import static org.motechproject.whp.common.util.WHPDate.DATE_TIME_FORMAT;

@Component
public class WeightStatisticsMapper {

    public WeightStatistics map(PatientWebRequest patientWebRequest) {
        WeightStatistics statistics = new WeightStatistics();
        if (isNotEmpty(patientWebRequest.getWeight_instance())) {
            LocalDate measuringDate = parse(patientWebRequest.getDate_modified(), forPattern(DATE_TIME_FORMAT));
            statistics.add(
                    SampleInstance.valueOf(patientWebRequest.getWeight_instance()),
                    Double.valueOf(patientWebRequest.getWeight()),
                    measuringDate
            );
        }
        return statistics;
    }
}
