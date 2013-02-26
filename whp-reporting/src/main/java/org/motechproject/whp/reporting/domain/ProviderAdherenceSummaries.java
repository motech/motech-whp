package org.motechproject.whp.reporting.domain;

import lombok.Data;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.core.Is.is;

@Data
public class ProviderAdherenceSummaries {
    private String district;
    private List<ProviderAdherenceSummary> adherenceSummaryList;

    public ProviderAdherenceSummaries() {
    }

    public ProviderAdherenceSummaries(String district, List<ProviderAdherenceSummary> adherenceSummaryList) {
        this.district = district;
        this.adherenceSummaryList = adherenceSummaryList;
    }

    public List<ProviderAdherenceSummary> getPendingAdherenceSummaryList() {
        return filter(having(on(ProviderAdherenceSummary.class).isAdherenceGiven(), is(false)), adherenceSummaryList);
    }

    public List<ProviderAdherenceSummary> getAdherenceGivenSummaryList() {
        return filter(having(on(ProviderAdherenceSummary.class).isAdherenceGiven(), is(true)), adherenceSummaryList);
    }
}
