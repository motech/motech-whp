package org.motechproject.whp.it.patient.repository.allPatients;

import ch.lambdaj.Lambda;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.common.domain.alerts.PatientAlertType;
import org.motechproject.whp.common.util.WHPDate;
import org.motechproject.whp.patient.builder.PatientBuilder;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentCategory;
import org.motechproject.whp.patient.domain.TreatmentOutcome;
import org.motechproject.whp.patient.model.AlertTypeFilter;
import org.motechproject.whp.patient.model.AlertTypeFilters;
import org.motechproject.whp.patient.model.FlagFilter;
import org.motechproject.whp.patient.query.PatientQueryDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.equalTo;
import static org.joda.time.DateTime.now;
import static org.motechproject.whp.patient.model.AlertTypeFilters.ADHERENCE_MISSING_WITH_SEVERITY_ONE;

public class PatientFilterTestPart  extends AllPatientsTestPart {

    private Patient patient1;
    private Patient patient2;
    private Patient patient3;
    private Patient patient4WithoutAlerts;
    private Patient inactivePatient;
    private final String UNIQUE_TREATMENT_CATEGORY_CODE = "05";

    @Autowired
    AlertTypeFilters alertTypeFilters;

    @Before
    public void setUp() {
        allPatients.removeAll();

        patient1 = new PatientBuilder().withDefaults()
                .withPatientId("123")
                .withProviderId("provider1")
                .withProviderDistrict("district")
                .withCumulativeMissedAlertValue(10, 1, DateUtil.today())
                .withAdherenceMissedWeeks(3, 1, DateUtil.today().minusDays(7))
                .withTreatmentNotStartedDays(0, 0, DateUtil.today())
                .withCpProgressAlert(110, 1, DateUtil.today())
                .withPatientFlag(true).build();

        patient2 = new PatientBuilder().withDefaults()
                .withPatientId("456")
                .withProviderId("provider1")
                .withProviderDistrict("district")
                .withCumulativeMissedAlertValue(11, 1, DateUtil.today())
                .withAdherenceMissedWeeks(7, 2, DateUtil.today().minusDays(5))
                .withTreatmentNotStartedDays(0, 0, DateUtil.today())
                .withIpProgressAlert(110, 1, DateUtil.today()).build();

        patient3 = new PatientBuilder().withDefaults()
                .withPatientId("789")
                .withProviderId("provider2")
                .withProviderDistrict("district2")
                .withCumulativeMissedAlertValue(11, 1, DateUtil.today())
                .withAdherenceMissedWeeks(7, 2, DateUtil.today().minusDays(5))
                .withTreatmentNotStartedDays(0, 0, DateUtil.today()).build();

        patient4WithoutAlerts = new PatientBuilder().withDefaults()
                .withPatientId("patient4")
                .withProviderId("provider3")
                .withProviderDistrict("district3")
                .build();

        TreatmentCategory treatmentCategory = new TreatmentCategory();
        treatmentCategory.setName("Unique treatment category");
        treatmentCategory.setCode(UNIQUE_TREATMENT_CATEGORY_CODE);
        patient4WithoutAlerts.getCurrentTherapy().setTreatmentCategory(treatmentCategory);

        inactivePatient = new PatientBuilder().withDefaults()
                .withPatientId("patient5")
                .withProviderId("provider3")
                .withProviderDistrict("district3").build();
        inactivePatient.closeCurrentTreatment(TreatmentOutcome.Cured, now());

        allPatients.add(patient1);
        allPatients.add(patient2);
        allPatients.add(patient3);
        allPatients.add(patient4WithoutAlerts);
        allPatients.add(inactivePatient);
    }

    @Test
    public void shouldReturnAllActivePatients() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(4, searchResults.size());
        assertEquals(4, allPatients.count(queryParams));
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldReturnAllActivePatientsWithAlerts() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("hasNotifiableAlerts", "true");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(3, searchResults.size());
        assertEquals(3, allPatients.count(queryParams));
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldReturnAllActivePatientsWithGivenTreatmentCategory() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("treatmentCategory", UNIQUE_TREATMENT_CATEGORY_CODE);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(1, allPatients.count(queryParams));
        assertEquals(patient4WithoutAlerts.getPatientId(), searchResults.get(0).getPatientId());
    }

    @Test
    public void shouldFilterPatientsByProviderDistrict() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerDistrict", "district");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(2, searchResults.size());
        assertEquals(2, allPatients.count(queryParams));
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldFilterPatientsByProviderId() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(2, searchResults.size());
        assertEquals(2, allPatients.count(queryParams));
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldFilterPatientsByCumulativeMissedDoses() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put(PatientQueryDefinition.cumulativeMissedDosesFromParam(), 10);
        queryParams.put(PatientQueryDefinition.cumulativeMissedDosesFromParam(), Integer.MAX_VALUE);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(2, searchResults.size());
        assertEquals(2, allPatients.count(queryParams));
        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
        assertEquals(patient2.getPatientId(), searchResults.get(1).getPatientId());
        assertTrue(hasNoInactivePatients(searchResults));
    }


    @Test
    public void shouldFilterPatientsByAdherenceMissingWeeks() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put(PatientQueryDefinition.adherenceMissingWeeksFromParam(), 3);
        queryParams.put(PatientQueryDefinition.adherenceMissingWeeksToParam(), Integer.MAX_VALUE);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(2, searchResults.size());
        assertEquals(2, allPatients.count(queryParams));
        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
        assertEquals(patient2.getPatientId(), searchResults.get(1).getPatientId());
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldFilterPatientsByPatientAlertsSeverity() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put(PatientAlertType.AdherenceMissing.name() + PatientQueryDefinition.ALERT_SEVERITY, 2);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(1, allPatients.count(queryParams));
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
        assertTrue(hasNoInactivePatients(searchResults));
    }


    @Test
    public void shouldFilterPatientsByPatientAlertsDateRange() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        String alertDateFrom = WHPDate.date(DateUtil.today().minusDays(5)).value();
        String alertDateTo = WHPDate.date(DateUtil.today().minusDays(5)).value();
        queryParams.put(PatientQueryDefinition.alertDateFromParam(), alertDateFrom);
        queryParams.put(PatientQueryDefinition.alertDateToParam(), alertDateTo);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(1, allPatients.count(queryParams));
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldFilterPatientsByFlag() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        queryParams.put("flag", FlagFilter.True.getValue());

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(1, allPatients.count(queryParams));
        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
        assertTrue(hasNoInactivePatients(searchResults));
    }

    @Test
    public void shouldFilterPatientsByAlertTypeAndDateCombined() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        String alertDateFrom = WHPDate.date(DateUtil.today().minusDays(10)).value();
        String alertDateTo = WHPDate.date(DateUtil.today().minusDays(7)).value();
        queryParams.put(PatientAlertType.AdherenceMissing.name() + "AlertDateFrom", alertDateFrom);
        queryParams.put(PatientAlertType.AdherenceMissing.name() + "AlertDateTo", alertDateTo);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(1, allPatients.count(queryParams));
    }

    @Test
    public void shouldFilterPatientsByAlertTypeAndDateCombinedAndNotReturnAnything() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        String alertDateFrom = WHPDate.date(DateUtil.today().minusDays(10)).value();
        String alertDateTo = WHPDate.date(DateUtil.today().minusDays(7)).value();
        queryParams.put(PatientAlertType.CumulativeMissedDoses.name() + "AlertDateFrom", alertDateFrom);
        queryParams.put(PatientAlertType.CumulativeMissedDoses.name() + "AlertDateTo", alertDateTo);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(0, searchResults.size());
        assertEquals(0, allPatients.count(queryParams));
    }

    @Test
    public void shouldFilterPatientsByAlertTypeAndDateCombinedAlongWithSeverity() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put("providerId", "provider1");
        String alertDateFrom = WHPDate.date(DateUtil.today().minusDays(10)).value();
        String alertDateTo = WHPDate.date(DateUtil.today().minusDays(5)).value();
        queryParams.put(PatientAlertType.AdherenceMissing.name() + "AlertDateFrom", alertDateFrom);
        queryParams.put(PatientAlertType.AdherenceMissing.name() + "AlertDateTo", alertDateTo);
        AlertTypeFilter filter = alertTypeFilters.getFilter(ADHERENCE_MISSING_WITH_SEVERITY_ONE);
        queryParams.put(filter.getFilterKey(), filter.getFilterValue());

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
        assertEquals(1, allPatients.count(queryParams));
    }

    @Test
    public void shouldSortPatientsBasedOnCumulativeMissedDoseValue() {
        SortParams sortParams = new SortParams();
        sortParams.put(PatientAlertType.CumulativeMissedDoses.name() + "AlertValue", "DESC");
        FilterParams queryParams = new FilterParams();

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(4, searchResults.size());
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
        assertEquals(patient3.getPatientId(), searchResults.get(1).getPatientId());
        assertEquals(patient1.getPatientId(), searchResults.get(2).getPatientId());
        assertEquals(patient4WithoutAlerts.getPatientId(), searchResults.get(3).getPatientId());
    }

    @Test
    public void shouldReturnPatientsBasedOnCPProgressAlertTypes() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put(PatientAlertType.CPProgress.name() + "AlertValueFrom", 110);
        queryParams.put(PatientAlertType.CPProgress.name() + "AlertValueTo", Integer.MAX_VALUE);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient1.getPatientId(), searchResults.get(0).getPatientId());
    }

    @Test
    public void shouldReturnPatientsBasedOnIPProgressAlertTypes() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put(PatientAlertType.IPProgress.name() + "AlertValueFrom", 110);
        queryParams.put(PatientAlertType.IPProgress.name() + "AlertValueTo", Integer.MAX_VALUE);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(1, searchResults.size());
        assertEquals(patient2.getPatientId(), searchResults.get(0).getPatientId());
    }

    @Test
    public void shouldReturnPatientsBasedOnCPAndIPProgressAlertTypes() {
        SortParams sortParams = new SortParams();
        FilterParams queryParams = new FilterParams();
        queryParams.put(PatientAlertType.IPProgress.name() + "AlertValueFrom", 120);
        queryParams.put(PatientAlertType.IPProgress.name() + "AlertValueTo", Integer.MAX_VALUE);
        queryParams.put(PatientAlertType.CPProgress.name() + "AlertValueFrom", 120);
        queryParams.put(PatientAlertType.CPProgress.name() + "AlertValueTo", Integer.MAX_VALUE);

        List<Patient> searchResults =  allPatients.filter(queryParams, sortParams, 0, 5);

        assertEquals(0, searchResults.size());
    }

    private boolean hasNoInactivePatients(List<Patient> searchResults) {
        return Lambda.filter(having(on(Patient.class).isOnActiveTreatment(), equalTo(false)), searchResults).size() == 0;
    }
}
