package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import org.motechproject.whp.patient.query.PatientQueryDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.motechproject.whp.patient.model.AlertTypeFilters.ALL_ALERTS;
import static org.motechproject.whp.patient.model.AlertTypeFilters.NO_ALERTS;

@Component
@EqualsAndHashCode
public class AlertDateFilters {

    private AlertTypeFilters alertTypeFilters;

    private List<AlertDateFilter> alertDateFilters = new ArrayList<>();

    @Autowired
    public AlertDateFilters(AlertTypeFilters alertTypeFilters) {
        this.alertTypeFilters = alertTypeFilters;
        alertDateFilters.addAll(asList(AlertDateFilter.values()));
    }

    public Map<String, Object> getQueryFieldsForType(String enumValue, String alertType) {
        Map<String, Object> queryFields = new HashMap<>();

        AlertDateFilter alertDateFilter = AlertDateFilter.valueOf(enumValue);
        AlertTypeFilter alertTypeFilter = alertTypeFilters.getFilter(alertType);

        if(isSpecificAlertType(alertType)) {
            queryFields.put(PatientQueryDefinition.alertDateFromParamForType(alertTypeFilter.getAlertType()), alertDateFilter.getFrom());
            queryFields.put(PatientQueryDefinition.alertDateToParamForType(alertTypeFilter.getAlertType()), alertDateFilter.getTo());
            return queryFields;
        }
        return AlertDateFilter.getQueryFields(enumValue);
    }

    private boolean isSpecificAlertType(String alertType) {
        return (!ALL_ALERTS.equals(alertType) && !NO_ALERTS.equals(alertType));
    }
}
