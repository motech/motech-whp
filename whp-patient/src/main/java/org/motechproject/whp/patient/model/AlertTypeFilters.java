package org.motechproject.whp.patient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class AlertTypeFilters {
    private List<AlertTypeFilter> alertTypeFilters = new ArrayList<>();

    public AlertTypeFilters() {
        alertTypeFilters.addAll(asList(AlertTypeFilter.values()));
    }
}
