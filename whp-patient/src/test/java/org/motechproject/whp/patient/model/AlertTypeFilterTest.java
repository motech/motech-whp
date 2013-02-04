package org.motechproject.whp.patient.model;

import org.junit.Test;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.patient.query.PatientQueryDefinition;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class AlertTypeFilterTest {
    @Test
    public void shouldReturnQueryFieldsBasedOnGivenEnumValue(){
        Map<String,Object> queryFields = AlertTypeFilter.getQueryFields(AlertTypeFilter.AdherenceMissingWithSeverityTwo.name());

        assertEquals(1, queryFields.size());
        assertEquals(AlertTypeFilter.AdherenceMissingWithSeverityTwo.getFilterValue(), queryFields.get(PatientQueryDefinition.alertSeverityParam(PatientAlertType.AdherenceMissing)));
    }
}
