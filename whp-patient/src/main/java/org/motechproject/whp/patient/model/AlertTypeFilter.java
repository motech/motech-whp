package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;

import java.util.HashMap;
import java.util.Map;

@Getter
@EqualsAndHashCode
public class AlertTypeFilter {
    private String name;
    private String messageCode;
    private String color;
    private String filterKey;
    private String filterValue;
    private PatientAlertType alertType;

    public AlertTypeFilter(String name) {
        this.name = name;
    }

    AlertTypeFilter(String name, String messageCode, String color, PatientAlertType alertType, String filterKey, String filterValue) {
        this.name = name;
        this.messageCode = messageCode;
        this.color = color;
        this.filterKey = filterKey;
        this.filterValue = filterValue;
        this.alertType = alertType;
    }

    public Map<String, Object> getQueryFields() {
        Map<String, Object> queryFields = new HashMap<>();
        queryFields.put(filterKey, getFilterValue());
        return queryFields;
    }

    public AlertTypeFilter withMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public AlertTypeFilter withColor(String color) {
        this.color = color;
        return this;
    }

    public AlertTypeFilter withFilterKey(String filterKey) {
        this.filterKey = filterKey;
        return this;
    }

    public AlertTypeFilter withFilterValue(String filterValue) {
        this.filterValue = filterValue;
        return this;
    }

    public AlertTypeFilter withAlertType(PatientAlertType alertType) {
        this.alertType = alertType;
        return this;
    }
}