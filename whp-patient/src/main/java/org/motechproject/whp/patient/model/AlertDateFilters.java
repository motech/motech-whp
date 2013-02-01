package org.motechproject.whp.patient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class AlertDateFilters {
    private List<AlertDateFilter> alertTypeFilterList = new ArrayList<>();

    public AlertDateFilters() {
        alertTypeFilterList.addAll(asList(AlertDateFilter.values()));
    }
}
