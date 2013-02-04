package org.motechproject.whp.common.service;

import lombok.Setter;
import org.motechproject.model.DayOfWeek;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.model.DayOfWeek.getDayOfWeek;

@Setter
@Component
public class AlertsPropertiesValues {

    @Value("#{alertsProperties['alerts.adherenceMissingWeeksThreshold'].split(',')}")
    private List<String> adherenceMissingWeeks;

    @Value("#{alertsProperties['alerts.cumulativeMissedDosesThreshold']}")
    private String cumulativeMissedDoses;

    @Value("#{alertsProperties['alerts.treatmentNotStartedDaysThreshold']}")
    private String treatmentNotStartedDays;

    @Value("#{alertsProperties['alerts.daysOfAlertGenerationForCumulativeMissedDoses'].split(',')}")
    private List<String> daysOfAlertGenerationForCumulativeDoses;

    @Value("#{alertsProperties['alerts.daysOfAlertGenerationForAdherenceMissingWeeks'].split(',')}")
    private List<String> daysOfAlertGenerationForAdherenceMissingWeeks;

    @Value("#{alertsProperties['alerts.daysOfAlertGenerationForTreatmentNotStarted'].split(',')}")
    private List<String> daysOfAlertGenerationForTreatmentNotStarted;


    public AlertsPropertiesValues() {
    }

    public List<Integer> getAdherenceMissingWeeks() {
        return parseToIntegerList(adherenceMissingWeeks);
    }

    private List<Integer> parseToIntegerList(List<String> stringList) {
        List<Integer> integerList = new ArrayList<>();

        for (String stringValue : stringList) {
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

    public List<DayOfWeek> getDayOfAlertGenerationForCumulativeMissedDoses() {
        return getDayOfWeeks(daysOfAlertGenerationForCumulativeDoses);
    }

    public List<DayOfWeek> getDaysOfAlertGenerationForAdherenceMissingWeeks() {
        return getDayOfWeeks(daysOfAlertGenerationForAdherenceMissingWeeks);
    }

    public List<DayOfWeek> getDayOfAlertGenerationForTreatmentNotStarted() {
        return getDayOfWeeks(daysOfAlertGenerationForTreatmentNotStarted);
    }

    private List<DayOfWeek> getDayOfWeeks(List<String> dayOfAlertGeneration) {
        List<DayOfWeek> daysOfWeek = new ArrayList<>();

        for (String dayOfWeek : dayOfAlertGeneration) {
            daysOfWeek.add(DayOfWeek.getDayOfWeek(Integer.parseInt(dayOfWeek)));
        }
        return daysOfWeek;
    }
}
