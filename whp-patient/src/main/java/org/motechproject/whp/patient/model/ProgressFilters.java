package org.motechproject.whp.patient.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Component
@EqualsAndHashCode
public class ProgressFilters {

    @Getter
    private List<ProgressFilter> progressFilters = new ArrayList<>();

    public ProgressFilters() {
        progressFilters.addAll(asList(ProgressFilter.values()));
    }
}
