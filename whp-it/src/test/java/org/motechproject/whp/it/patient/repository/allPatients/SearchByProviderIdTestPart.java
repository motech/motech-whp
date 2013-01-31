package org.motechproject.whp.it.patient.repository.allPatients;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.paginator.contract.FilterParams;
import org.motechproject.paginator.contract.SortParams;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.patient.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class SearchByProviderIdTestPart extends AllPatientsTestPart {
    FilterParams filterParams;

    SortParams sortParams;


    @Before
    public void setUp() {
        allPatients.removeAll();
    }

    @Test
    public void shouldFetchPatientsByCurrentProviderId() {
        Patient requiredPatient = createPatient("patientId1", "providerId1", PROVIDER_DISTRICT);
        createPatient("patientId2", "providerId2", PROVIDER_DISTRICT);

        assertPatientEquals(new Patient[]{requiredPatient}, allPatients.getAllWithActiveTreatmentFor("providerId1").toArray());
    }

    @Test
    public void shouldFetchPatientsUnderActiveTreatmentByProvider() {
        String providerId = "providerId1";
        Patient requiredPatient = createPatient("patientId1", providerId, PROVIDER_DISTRICT);

        Patient patientWithTreatmentClosed = createPatient("patientId2", providerId, PROVIDER_DISTRICT);
        patientWithTreatmentClosed.closeCurrentTreatment(TreatmentOutcome.Died, DateUtil.now());
        allPatients.update(patientWithTreatmentClosed);

        assertPatientEquals(new Patient[]{requiredPatient}, allPatients.getAllWithActiveTreatmentFor(providerId).toArray());
    }

    @Test
    public void fetchByCurrentProviderIdShouldReturnEmptyListIfKeywordIsNull() {
        assertEquals(new ArrayList<Patient>(), allPatients.getAllWithActiveTreatmentFor(null));
    }

    @Test
    public void shouldFetchAllPatientsForAProvider() {
        Patient patient1 = createPatient("patientId1", "providerId1", PROVIDER_DISTRICT);
        Patient patient2 = createPatient("patientId2", "providerId1", PROVIDER_DISTRICT);

        assertPatientEquals(new Patient[]{patient1, patient2}, allPatients.getAllWithActiveTreatmentFor("providerId1").toArray());
    }

    @Test
    public void shouldFetchPatientsForAProviderWithActiveTreatment() {
        Patient withoutActiveTreatment1 = createPatient("patientId1", "providerId1", PROVIDER_DISTRICT);
        withoutActiveTreatment1.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        allPatients.update(withoutActiveTreatment1);

        Patient withActiveTreatment1 = createPatient("patientId2", "providerId1", PROVIDER_DISTRICT);

        Patient withoutActiveTreatment2 = createPatient("patientId3", "providerId2", PROVIDER_DISTRICT);
        withoutActiveTreatment2.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        allPatients.update(withoutActiveTreatment2);

        Patient withActiveTreatment2 = createPatient("patientId4", "providerId2", PROVIDER_DISTRICT);

        assertPatientEquals(new Patient[]{withActiveTreatment1}, allPatients.getAllWithActiveTreatmentFor("providerId1").toArray());
        assertPatientEquals(new Patient[]{withActiveTreatment2}, allPatients.getAllWithActiveTreatmentFor("providerId2").toArray());
    }

    @Test
    public void shouldFetchPatientsForProvider_sortByTreatmentStartDate() {
        String providerId = "providerId1";

        DateTime oldestTreatmentStartDate = now().minusDays(5);
        DateTime olderTreatmentStartDate = now().minusDays(2);
        DateTime newerTreatmentStartDate = now();

        Patient withOldestStartDate = createPatientOnActiveTreatment("patientId1", "c", providerId, PROVIDER_DISTRICT, oldestTreatmentStartDate);
        Patient withNewerStartDate = createPatientOnActiveTreatment("patientId2", "b", providerId, PROVIDER_DISTRICT, newerTreatmentStartDate);
        Patient withOlderStartDate = createPatientOnActiveTreatment("patientId3", "a", providerId, PROVIDER_DISTRICT, olderTreatmentStartDate);

        assertPatientEquals(
                asList(withOldestStartDate, withOlderStartDate, withNewerStartDate),
                allPatients.getAllWithActiveTreatmentFor(providerId)
        );
    }

    @Test
    public void shouldFetchAllPatientsUnderAProviderByPage(){
        int startIndex = 0;       //page starts from zero
        int rowsPerPage = 2;
        String providerId = "provider";

        filterParams = new FilterParams();
        filterParams.put("selectedProvider", providerId);

        String district = "district";
        createPatient("id1", "providerid1", district);
        Patient patient1 = createPatient("id2", providerId, district);
        Patient patient2 = createPatient("id3", providerId, district);
        createPatient("id4", providerId, district);
        createPatient("id5", providerId, district);
        Patient patient6 = createPatient("id6", providerId, district);

        List<Patient> patientsForFirstPage = allPatients.filter(filterParams, sortParams, startIndex, rowsPerPage);

        assertThat(patientsForFirstPage.size(), is(2));
        assertPatientEquals(asList(patient1,patient2), patientsForFirstPage);

        startIndex = 2;
        List<Patient> patientsForThirdPage = allPatients.filter(filterParams, sortParams, startIndex, rowsPerPage);
        assertThat(patientsForThirdPage.size(),is(1));
        assertPatientEquals(patient6, patientsForThirdPage.get(0));
    }
}
