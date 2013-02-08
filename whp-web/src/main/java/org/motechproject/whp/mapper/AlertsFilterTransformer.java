package org.motechproject.whp.mapper;

import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.whp.patient.model.AlertDateFilter;
import org.motechproject.whp.patient.model.AlertTypeFilter;
import org.motechproject.whp.patient.query.PatientQueryDefinition;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.whp.patient.model.AlertDateFilter.getQueryFields;

@Component
public class AlertsFilterTransformer {

    public FilterParams transform(FilterParams filterParams) {
        FilterParams transformedFilterParams = new FilterParams();
        for (Map.Entry<String, Object> entry : filterParams.entrySet()) {
            switch (entry.getKey()) {
                case "alertDate":
                    if (filterParams.containsKey("alertType")) {
                        transformedFilterParams.putAll(AlertDateFilter.getQueryFieldsForType((String) entry.getValue(), (String) filterParams.get("alertType")));
                    } else {
                        transformedFilterParams.putAll(getQueryFields((String) entry.getValue()));
                    }
                    break;
                case "alertType":
                    transformedFilterParams.putAll(AlertTypeFilter.getQueryFields((String) entry.getValue()));
                    break;
                case "cumulativeMissedDoses":
                    transformedFilterParams.put(PatientQueryDefinition.cumulativeMissedDosesFromParam(), entry.getValue());
                    transformedFilterParams.put(PatientQueryDefinition.cumulativeMissedDosesToParam(), Integer.MAX_VALUE);
                    break;
                case "adherenceMissingWeeks":
                    transformedFilterParams.put(PatientQueryDefinition.adherenceMissingWeeksFromParam(), entry.getValue());
                    transformedFilterParams.put(PatientQueryDefinition.adherenceMissingWeeksToParam(), Integer.MAX_VALUE);
                    break;
                default:
                    transformedFilterParams.put(entry.getKey(), entry.getValue());
            }
        }
        return transformedFilterParams;
    }
}
