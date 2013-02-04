package org.motechproject.whp.patient.query;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.CumulativeMissedDoses;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.ALERT_DATE;
import static org.motechproject.whp.patient.query.PatientQueryDefinition.ALERT_SEVERITY;

public class PatientQueryDefinitionTest {

    @Test
    public void shouldReturnFilterParamsForGivenAlertTypes() {
        assertEquals(AdherenceMissing.name() + ALERT_DATE, PatientQueryDefinition.alertDateParamForType(AdherenceMissing));
        assertEquals(AdherenceMissing.name() + ALERT_SEVERITY, PatientQueryDefinition.alertSeverityParam(AdherenceMissing));
        assertEquals(CumulativeMissedDoses.name() + ALERT_SEVERITY, PatientQueryDefinition.alertSeverityParam(CumulativeMissedDoses));
        assertEquals(CumulativeMissedDoses.name() + ALERT_DATE, PatientQueryDefinition.alertDateParamForType(CumulativeMissedDoses));
    }

    @Test
    public void shouldReturnFieldNameForPatientHasAlertsField() {
        assertEquals("hasAlerts", PatientQueryDefinition.alertStatusFieldName());
    }

}
