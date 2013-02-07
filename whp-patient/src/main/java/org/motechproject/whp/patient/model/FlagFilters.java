package org.motechproject.whp.patient.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Data
public class FlagFilters {
    private List<FlagFilter> flagFilters = new ArrayList<>();

    public FlagFilters() {
        flagFilters.addAll(asList(FlagFilter.values()));
    }
}
