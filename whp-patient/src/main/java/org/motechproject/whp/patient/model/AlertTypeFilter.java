package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class AlertTypeFilter {
    private final String name;
    private final String messageCode;
    private final String filterKey;
    private final String filterValue;
    private final PatientAlertType alertType;

    AlertTypeFilter(String name, String messageCode, String filterKey, String filterValue, PatientAlertType alertType) {
        this.name = name;
        this.messageCode = messageCode;
        this.filterKey = filterKey;
        this.filterValue = filterValue;
        this.alertType = alertType;
    }

    public Map<String, Object> getQueryFields() {
        Map<String, Object> queryFields = new HashMap<>();
        queryFields.put(filterKey, getFilterValue());
        return queryFields;
    }
}