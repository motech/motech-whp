package org.motechproject.whp.mapper;

import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.whp.patient.model.AlertTypeFilter;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.whp.patient.model.AlertDateFilter.getQueryFields;

@Component
public class AlertsFilterTransformer {

    public FilterParams transform(FilterParams filterParams) {
        FilterParams transformedFilterParams = new FilterParams();
        for (Map.Entry<String, Object> entry : filterParams.entrySet()) {
            if (entry.getKey().equals("alertDate")) {
                transformedFilterParams.putAll(getQueryFields((String) entry.getValue()));
            }
            if (entry.getKey().equals("alertType")) {
                transformedFilterParams.putAll(AlertTypeFilter.getQueryFields((String) entry.getValue()));
            }
            transformedFilterParams.put(entry.getKey(), entry.getValue());
        }
        return transformedFilterParams;
    }
}
