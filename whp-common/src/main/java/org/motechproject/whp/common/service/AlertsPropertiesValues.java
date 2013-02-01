package org.motechproject.whp.common.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Setter
@Component
public class AlertsPropertiesValues {

    @Value("#{alertsProperties['alerts.adherence.missing.numberOfWeeks'].split(',')}")
    private List<String> adherenceMissingWeeks;

    @Value("#{alertsProperties['alerts.cumulativeMissedDoses']}")
    private String cumulativeMissedDoses;

    @Value("#{alertsProperties['alerts.treatmentNotStartedDays']}")
    private String treatmentNotStartedDays;

    public AlertsPropertiesValues() {
    }

    public List<Integer> getAdherenceMissingWeeks(){
        return parseToIntegerList(adherenceMissingWeeks);
    }

    private List<Integer> parseToIntegerList(List<String> stringList) {
        List<Integer> integerList = new ArrayList<>();

        for (String stringValue : stringList){
            integerList.add(Integer.parseInt(stringValue));

        }
        return integerList;
    }

    public Integer getCumulativeMissedDoses() {
        return Integer.parseInt(cumulativeMissedDoses);
    }

    public Integer getTreatmentNotStartedDays() {
        return Integer.parseInt(treatmentNotStartedDays);
    }
}
