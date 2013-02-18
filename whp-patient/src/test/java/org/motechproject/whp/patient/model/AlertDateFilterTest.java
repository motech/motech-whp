package org.motechproject.whp.patient.model;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.whp.common.domain.alerts.AlertColorConfiguration;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.query.PatientQueryDefinition;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.whp.patient.model.AlertDateFilter.*;

public class AlertDateFilterTest {

    @Mock
    private AlertColorConfiguration alertColorConfiguration;
    private AlertTypeFilters alertTypeFilters;
    private AlertDateFilters alertDateFilters;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        alertTypeFilters = new AlertTypeFilters(alertColorConfiguration);
        alertDateFilters = new AlertDateFilters(alertTypeFilters);
    }

    @Test
    public void shouldReturnDateRangesGivenTheDateFilter(){
        assertEquals(WHPDate.date(new DateTime(1900, 1, 1, 0, 0, 0).toLocalDate()).value(), TillDate.getFrom());
        assertEquals(WHPDate.date(LocalDate.now()).value(), TillDate.getTo());

        assertEquals(WHPDate.date(LocalDate.now()).value(), Today.getFrom());
        assertEquals(WHPDate.date(LocalDate.now()).value(), Today.getTo());

        assertEquals(WHPDate.date(LocalDate.now().minusWeeks(1)).value(), ThisWeek.getFrom());
        assertEquals(WHPDate.date(LocalDate.now()).value(), ThisWeek.getTo());

        assertEquals(WHPDate.date(LocalDate.now().minusMonths(1)).value(), ThisMonth.getFrom());
        assertEquals(WHPDate.date(LocalDate.now()).value(), ThisMonth.getTo());
    }

    @Test
    public void shouldReturnQueryFieldsGivenAnEnumValue(){
        Map<String,Object> queryFields = AlertDateFilter.getQueryFields(AlertDateFilter.ThisMonth.name());

        assertEquals(2, queryFields.size());
        assertEquals(WHPDate.date(LocalDate.now().minusMonths(1)).value(), queryFields.get(PatientQueryDefinition.alertDateFromParam()));
        assertEquals(WHPDate.date(LocalDate.now()).value(), queryFields.get(PatientQueryDefinition.alertDateToParam()));
    }

    @Test
    public void shouldReturnQueryFieldsForGivenAlertDateAndType() {
        Map<String,Object> queryFields = alertDateFilters.getQueryFieldsForType(AlertDateFilter.ThisMonth.name(), "AdherenceMissingWithSeverityOne");

        assertEquals(2, queryFields.size());
        assertEquals(WHPDate.date(LocalDate.now().minusMonths(1)).value(), queryFields.get(PatientQueryDefinition.alertDateFromParamForType(PatientAlertType.AdherenceMissing)));
        assertEquals(WHPDate.date(LocalDate.now()).value(), queryFields.get(PatientQueryDefinition.alertDateToParamForType(PatientAlertType.AdherenceMissing)));
    }

    @Test
    public void shouldReturnQueryFieldsForTypeGivenAlertDateAndAllAlertsAlertType() {
        Map<String,Object> queryFields = alertDateFilters.getQueryFieldsForType(AlertDateFilter.ThisMonth.name(), AlertTypeFilters.ALL_ALERTS);

        assertEquals(2, queryFields.size());
        assertEquals(WHPDate.date(LocalDate.now().minusMonths(1)).value(), queryFields.get(PatientQueryDefinition.alertDateFromParam()));
        assertEquals(WHPDate.date(LocalDate.now()).value(), queryFields.get(PatientQueryDefinition.alertDateToParam()));
    }

    @Test
    public void shouldReturnQueryFieldsForTypeGivenAlertDateAndNoAlertsAlertType() {
        Map<String,Object> queryFields = alertDateFilters.getQueryFieldsForType(AlertDateFilter.ThisMonth.name(), AlertTypeFilters.NO_ALERTS);

        assertEquals(2, queryFields.size());
        assertEquals(WHPDate.date(LocalDate.now().minusMonths(1)).value(), queryFields.get(PatientQueryDefinition.alertDateFromParam()));
        assertEquals(WHPDate.date(LocalDate.now()).value(), queryFields.get(PatientQueryDefinition.alertDateToParam()));
    }
}
