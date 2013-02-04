package org.motechproject.whp.mapper;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDate;
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
        assertEquals(WHPDate.date(LocalDate.now()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateFromParam()));
        assertEquals(WHPDate.date(LocalDate.now()).value(), transformedFilterParams.get(PatientQueryDefinition.alertDateToParam()));
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
}
