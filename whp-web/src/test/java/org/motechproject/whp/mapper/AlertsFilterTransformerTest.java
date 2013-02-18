package org.motechproject.whp.mapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.model.AlertDateFilter;
import org.motechproject.whp.patient.model.AlertDateFilters;
import org.motechproject.whp.patient.model.AlertTypeFilters;
import org.motechproject.whp.patient.query.PatientQueryDefinition;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.patient.model.AlertDateFilter.Today;
import static org.motechproject.whp.patient.model.AlertTypeFilters.ADHERENCE_MISSING_WITH_SEVERITY_THREE;

public class AlertsFilterTransformerTest {

    @Mock
    private AlertColorConfiguration alertColorConfiguration;
    private AlertTypeFilters alertTypeFilters;
    private AlertDateFilters alertDateFilters;
    private AlertsFilterTransformer alertsFilterTransformer;

    @Before
    public void setUp() {
        initMocks(this);

        alertTypeFilters = new AlertTypeFilters(alertColorConfiguration);
        alertDateFilters = new AlertDateFilters(alertTypeFilters);
        alertsFilterTransformer = new AlertsFilterTransformer(alertTypeFilters, alertDateFilters);
    }

    @Test
    public void shouldConvertAlertDateFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("alertDate", Today.name());

        FilterParams transformedFilterParams = alertsFilterTransformer.transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateFromParam()));
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateToParam()));
    }

    @Test
    public void shouldConvertAlertTypeFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("alertType", ADHERENCE_MISSING_WITH_SEVERITY_THREE);

        FilterParams transformedFilterParams = alertsFilterTransformer.transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE).getFilterValue(),
                transformedFilterParams.get(PatientQueryDefinition.alertSeverityParam(AdherenceMissing)));
    }

    @Test
    public void shouldConvertAlertTypeFilterAndDateFilterCombinedIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("alertType", AlertTypeFilters.ADHERENCE_MISSING_WITH_SEVERITY_THREE);
        filterParams.put("alertDate", AlertDateFilter.Today.name());

        FilterParams transformedFilterParams = alertsFilterTransformer.transform(filterParams);

        assertEquals(4, transformedFilterParams.size());
        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_THREE).getFilterValue(), transformedFilterParams.get(PatientQueryDefinition.alertSeverityParam(AdherenceMissing)));
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateFromParamForType(AdherenceMissing)));
        assertEquals(WHPDate.date(DateUtil.today()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateToParamForType(AdherenceMissing)));
    }

    @Test
    public void shouldConvertCumulativeDosesFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("cumulativeMissedDoses", 10);

        FilterParams transformedFilterParams = alertsFilterTransformer.transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(10, transformedFilterParams.get(PatientQueryDefinition.cumulativeMissedDosesFromParam()));
        assertEquals(Integer.MAX_VALUE, transformedFilterParams.get(PatientQueryDefinition.cumulativeMissedDosesToParam()));
    }

    @Test
    public void shouldConvertAdherenceMissingWeeksFilterIntoDesiredQueryFields(){
        FilterParams filterParams = new FilterParams();
        filterParams.put("treatmentCategory", "01");
        filterParams.put("adherenceMissingWeeks", 12);

        FilterParams transformedFilterParams = alertsFilterTransformer.transform(filterParams);

        assertEquals(transformedFilterParams.get("treatmentCategory"), "01");
        assertEquals(12, transformedFilterParams.get(PatientQueryDefinition.adherenceMissingWeeksFromParam()));
        assertEquals(Integer.MAX_VALUE, transformedFilterParams.get(PatientQueryDefinition.adherenceMissingWeeksToParam()));
    }
}
