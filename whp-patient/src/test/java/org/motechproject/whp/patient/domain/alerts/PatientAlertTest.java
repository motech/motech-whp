package org.motechproject.whp.patient.domain.alerts;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.testing.utils.BaseUnitTest;

import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.today;
import static org.motechproject.whp.common.domain.alerts.PatientAlertType.AdherenceMissing;

public class PatientAlertTest extends BaseUnitTest {

    @Test
    public void shouldUpdateAlertDetailsWhenSeverityHasChanged() {
        mockCurrentDate(today());

        PatientAlert patientAlert = new PatientAlert(AdherenceMissing);
        patientAlert.setAlertDate(today().minusDays(1));
        patientAlert.setAlertSeverity(1);
        patientAlert.setValue(10);

        int newValue = 13;
        int newSeverity = 2;

        patientAlert.update(newValue, newSeverity);

        assertEquals(newValue, patientAlert.getValue());
        assertEquals(newSeverity, patientAlert.getAlertSeverity());
        assertEquals(today(), patientAlert.getAlertDate());
        assertEquals(AdherenceMissing, patientAlert.getAlertType());
    }

    @Test
    public void shouldNotUpdateAlertDetailsWhenSeverityHasNotChanged() {
        mockCurrentDate(today());

        PatientAlert patientAlert = new PatientAlert(AdherenceMissing);
        int originalSeverity = 1;
        LocalDate originalAlertDate = today().minusDays(originalSeverity);
        patientAlert.setAlertDate(originalAlertDate);
        patientAlert.setAlertSeverity(originalSeverity);
        patientAlert.setValue(10);

        int newValue = 13;
        patientAlert.update(newValue, originalSeverity);

        assertEquals(newValue, patientAlert.getValue());
        assertEquals(originalSeverity, patientAlert.getAlertSeverity());
        assertEquals(originalAlertDate, patientAlert.getAlertDate());
        assertEquals(AdherenceMissing, patientAlert.getAlertType());
    }
}
