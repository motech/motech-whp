package org.motechproject.whp.mapper;

import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.model.AlertDateFilter;
import org.motechproject.whp.patient.model.AlertTypeFilter;
import org.motechproject.whp.patient.query.PatientQueryDefinition;

import static org.junit.Assert.assertEquals;
import static org.motechproject.whp.patient.model.AlertDateFilter.Today;

public class AlertsFilterTransformerTest {
    @Test
    public void shouldConvertAlertDateFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("alertDate", Today.name());

        FilterParams transformedFilterParams = new AlertsFilterTransformer().transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateFromParam()));
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateToParam()));
    }

    @Test
    public void shouldConvertAlertTypeFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("alertType", AlertTypeFilter.AdherenceMissingWithSeverityThree.name());

        FilterParams transformedFilterParams = new AlertsFilterTransformer().transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(AlertTypeFilter.AdherenceMissingWithSeverityThree.getFilterValue(), transformedFilterParams.get(PatientQueryDefinition.alertSeverityParam(PatientAlertType.AdherenceMissing)));
    }

    @Test
    public void shouldConvertAlertTypeFilterAndDateFilterCombinedIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("alertType", AlertTypeFilter.AdherenceMissingWithSeverityThree.name());
        filterParams.put("alertDate", AlertDateFilter.Today.name());

        FilterParams transformedFilterParams = new AlertsFilterTransformer().transform(filterParams);

        assertEquals(4, transformedFilterParams.size());
        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(AlertTypeFilter.AdherenceMissingWithSeverityThree.getFilterValue(), transformedFilterParams.get(PatientQueryDefinition.alertSeverityParam(PatientAlertType.AdherenceMissing)));
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateFromParamForType(PatientAlertType.AdherenceMissing)));
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateToParamForType(PatientAlertType.AdherenceMissing)));
    }

    @Test
    public void shouldConvertCumulativeDosesFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("cumulativeMissedDoses", 10);

        FilterParams transformedFilterParams = new AlertsFilterTransformer().transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(10, transformedFilterParams.get(PatientQueryDefinition.cumulativeMissedDosesFromParam()));
        assertEquals(Integer.MAX_VALUE, transformedFilterParams.get(PatientQueryDefinition.cumulativeMissedDosesToParam()));
    }

    @Test
    public void shouldConvertAdherenceMissingWeeksFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("adherenceMissingWeeks", 12);

        FilterParams transformedFilterParams = new AlertsFilterTransformer().transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(12, transformedFilterParams.get(PatientQueryDefinition.adherenceMissingWeeksFromParam()));
        assertEquals(Integer.MAX_VALUE, transformedFilterParams.get(PatientQueryDefinition.adherenceMissingWeeksToParam()));
    }
}
