package org.motechproject.whp.patient.model;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.query.PatientQueryDefinition;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.motechproject.whp.patient.model.AlertDateFilter.*;

public class AlertDateFilterTest {
    @Test
    public void shouldReturnDateRangesGivenTheDateFilter(){
        assertNull(TillDate.getFrom());
        assertNull(TillDate.getTo());

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
}
