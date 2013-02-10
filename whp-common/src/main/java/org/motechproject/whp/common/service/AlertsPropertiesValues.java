package org.motechproject.whp.common.service;

import lombok.Setter;
import org.motechproject.model.DayOfWeek;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

    @Value("#{alertsProperties['alerts.adherenceMissing.severity.colors'].split(',')}")
    private List<String> adherenceMissingSeverityColors;

    @Value("#{alertsProperties['alerts.cumulativeMissedDoses.severity.colors'].split(',')}")
    private List<String> cumulativeMissedDosesSeverityColors;

    @Value("#{alertsProperties['alerts.treatmentNotStarted.severity.colors'].split(',')}")
    private List<String> treatmentNotStartedSeverityColors;


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

    public List<String> getAdherenceMissingSeverityColors() {
        return addColorForEverySeverity(adherenceMissingSeverityColors);
    }

    private List<String> addColorForEverySeverity(List<String> alertColors) {
        String NO_COLOR_FOR_ZERO_SEVERITY = "";

        List<String> severityColors = new ArrayList<>();
        severityColors.add(NO_COLOR_FOR_ZERO_SEVERITY);
        severityColors.addAll(alertColors);
        return severityColors;
    }

    public List<String> getCumulativeMissedDosesSeverityColors() {
        return addColorForEverySeverity(cumulativeMissedDosesSeverityColors);
    }

    public List<String> getTreatmentNotStartedSeverityColors() {
        return addColorForEverySeverity(treatmentNotStartedSeverityColors);
    }
}
