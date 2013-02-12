package org.motechproject.whp.patient.model;

import lombok.Getter;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.util.WHPDate;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.whp.common.util.WHPDate.ARBITRARY_PAST_DATE;
import static org.motechproject.whp.common.util.WHPDate.today;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertDateFromParam;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.alertDateToParam;

@Getter
public enum AlertDateFilter {

    TillDate("Alerts raised till date"){
        @Override
        String getFrom() {
            return WHPDate.date(ARBITRARY_PAST_DATE).value();
        }

        @Override
        String getTo() {
            return today();
        }

    },
    Today("Alerts raised today") {
        @Override
        String getFrom() {
            return  today();
        }

        @Override
        String getTo() {
            return  today();
        }
    },
    ThisWeek("Alerts raised this week") {
        @Override
        String getFrom() {
            return WHPDate.date(DateUtil.today().minusWeeks(1)).value();  
        }

        @Override
        String getTo() {
            return today();  
        }
    },
    ThisMonth("Alerts raised this month") {
        @Override
        String getFrom() {
            return WHPDate.date(DateUtil.today().minusMonths(1)).value();  
        }

        @Override
        String getTo() {
            return today();  
        }
    };

    private String displayText;
    abstract String getFrom();
    abstract String getTo();

    AlertDateFilter(String displayText) {
        this.displayText = displayText;
    }

    public static Map<String, Object> getQueryFields(String enumValue) {
        Map<String, Object> queryFields = new HashMap<>();

        AlertDateFilter alertDateFilter = AlertDateFilter.valueOf(enumValue);
        queryFields.put(alertDateFromParam(), alertDateFilter.getFrom());
        queryFields.put(alertDateToParam(), alertDateFilter.getTo());
        return queryFields;
    }
}