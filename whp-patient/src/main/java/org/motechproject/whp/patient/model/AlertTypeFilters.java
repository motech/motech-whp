package org.motechproject.whp.patient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class AlertTypeFilters {
    private List<AlertTypeFilter> alertTypeFilterList = new ArrayList<>();

    public AlertTypeFilters() {
        alertTypeFilterList.addAll(asList(AlertTypeFilter.values()));
    }
}
