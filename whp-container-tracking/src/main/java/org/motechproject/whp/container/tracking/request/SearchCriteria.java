package org.motechproject.whp.container.tracking.request;

import java.util.*;

import static org.motechproject.whp.refdata.domain.SputumTrackingInstance.PreTreatment;

public class SearchCriteria {

    private final Map<String, String> searchCriteria = new HashMap<>();

    private static final List<String> searchKeys = Arrays.asList("providerId", "providerDistrict", "containerStatus",
            "cumulativeLabResult", "diagnosis", "reasonForClosure");

    public SearchCriteria(Properties filterParams) {
        mapStringSearchKeys(filterParams);
        mapDateRangeSearchCriteria(filterParams, "containerIssuedDateFrom", "containerIssuedDateTo", "containerIssuedDate");
        mapDateRangeSearchCriteria(filterParams, "consultationDateFrom", "consultationDateTo", "consultationDate");
    }

    public Map<String, String> preTreatmentCriteria() {
        Map<String, String> preTreatmentCriteria = new HashMap<>(searchCriteria);
        preTreatmentCriteria.put("containerInstance", PreTreatment.name());
        return searchCriteria;
    }

    private String getCriteriaForDateRange(Properties filterParams, String dateFrom, String dateTo) {
        return String.format("[%s TO %s]", filterParams.get(dateFrom), filterParams.get(dateTo));
    }

    private void mapDateRangeSearchCriteria(Properties filterParams, String dateFrom, String dateTo, String dateKey) {
        if (filterParams.contains(dateFrom)) {
            searchCriteria.put(dateKey + "<date>", getCriteriaForDateRange(filterParams, dateFrom, dateTo));
        }
    }

    private void mapStringSearchKeys(Properties filterParams) {
        for (Object key : filterParams.keySet()) {
            if (searchKeys.contains(key)) {
                searchCriteria.put(key.toString(), filterParams.get(key).toString());
            }
        }
    }
}
