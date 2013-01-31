package org.motechproject.whp.it.patient.repository.allPatients;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.query.PatientQueryDefinition;

import java.util.List;

import static junit.framework.Assert.assertEquals;

public class PatientFilterTestPart  extends AllPatientsTestPart {

    private Patient patient1;
    private Patient patient2;
    private Patient patient3;

    @Before
    public void setUp() {
        patient1 = new PatientBuilder().withDefaults()
                .withPatientId("patient1")
                .withProviderId("provider1")
                .withProviderDistrict("district")
                .withCumulativeMissedAlertValue(10, 1)
                .withAdherenceMissedWeeks(3, 1, DateUtil.today().minusDays(7))
                .withTreatmentNotStartedDays(0, 0).build();

        patient2 = new PatientBuilder().withDefaults()
                .withPatientId("patient2")
                .withProviderId("provider1")
                .withProviderDistrict("district")
                .withCumulativeMissedAlertValue(11, 1)
                .withAdherenceMissedWeeks(5, 2, DateUtil.today().minusDays(5))
                .withTreatmentNotStartedDays(0, 0).build();

        patient3 = new PatientBuilder().withDefaults()
                .withPatientId("patient3")
                .withProviderId("provider2")
                .withProviderDistrict("district2")
                .withCumulativeMissedAlertValue(11, 1)
                .withAdherenceMissedWeeks(5, 2, DateUtil.today().minusDays(5))
                .withTreatmentNotStartedDays(0, 0).build();

        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);
    }

    @Test
    public void shouldFilterPatientsByProviderDistrict() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerDistrict", "district");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(2, searchResults.size());
//        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
//        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
    }


    @Test
    public void shouldFilterPatientsByCumulativeMissedDoses() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put("cumulativeMissedDoses", "10");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
    }

    @Test
    public void shouldFilterPatientsByAdherenceMissingWeeks() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put("adherenceMissingWeeks", "5");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
    }

    @Test
    public void shouldFilterPatientsByPatientAlertsSeverity() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put(PatientAlertType.AdherenceMissing.name() + PatientQueryDefinition.ALERT_SEVERITY, "2");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
    }

    @Test
    public void shouldFilterPatientsByPatientAlertsDateRange() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        String alertDateFrom = WHPDate.date(DateUtil.today().minusDays(5)).value();
        String alertDateTo = WHPDate.date(DateUtil.today().minusDays(5)).value();
        queryParams.put(PatientAlertType.AdherenceMissing.name() + "AlertDateFrom", alertDateFrom);
        queryParams.put(PatientAlertType.AdherenceMissing.name() + "AlertDateTo", alertDateTo);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
    }

}
