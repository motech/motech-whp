package org.motechproject.whp.patient.model;

import lombok.Getter;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.util.WHPDate;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.whp.common.util.WHPDate.ARBITRARY_PAST_DATE;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertDateFromParam;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertDateToParam;

@Getter
public enum AlertDateFilter {

    TillDate("Alerts raised till date", WHPDate.date(ARBITRARY_PAST_DATE).value(), WHPDate.date(DateUtil.today()).value()),
    Today("Alerts raised today", WHPDate.date(DateUtil.today()).value(), WHPDate.date(DateUtil.today()).value()),
    ThisWeek("Alerts raised this week", WHPDate.date(DateUtil.today().minusWeeks(1)).value(), WHPDate.date(DateUtil.today()).value()),
    ThisMonth("Alerts raised this month", WHPDate.date(DateUtil.today().minusMonths(1)).value(), WHPDate.date(DateUtil.today()).value());

    private String displayText;
    private String from;
    private String to;

    AlertDateFilter(String displayText, String from, String to) {
        this.displayText = displayText;
        this.from = from;
        this.to = to;
    }

    public static Map<String, Object> getQueryFields(String enumValue) {
        Map<String, Object> queryFields = new HashMap<>();

        AlertDateFilter alertDateFilter = AlertDateFilter.valueOf(enumValue);
        queryFields.put(alertDateFromParam(), alertDateFilter.getFrom());
        queryFields.put(alertDateToParam(), alertDateFilter.getTo());
        return queryFields;
    }
}