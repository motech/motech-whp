package org.motechproject.whp.patient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class AlertDateFilters {
    private List<AlertDateFilter> alertDateFilters = new ArrayList<>();

    public AlertDateFilters() {
        alertDateFilters.addAll(asList(AlertDateFilter.values()));
    }
}
