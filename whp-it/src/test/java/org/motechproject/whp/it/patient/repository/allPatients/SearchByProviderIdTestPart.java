package org.motechproject.whp.it.patient.repository.allPatients;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
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
    public void shouldFetchByDistrict() {
        String provider1 = "provider1";
        String provider2 = "provider2";
        String provider3 = "provider3";

        Patient patient1 = createPatient("patientId1", provider1, PROVIDER_DISTRICT);
        Patient patient2 = createPatient("patientId2", provider2, "another-district");
        Patient patient3 = createPatient("patientId3", provider3, PROVIDER_DISTRICT);

        List<Patient> patients = allPatients.getAllUnderActiveTreatmentInDistrict(PROVIDER_DISTRICT);
        assertPatientEquals(asList(patient1, patient3), patients);
    }


    @Test
    public void shouldFetchByDistrictForGivenPageNumber(){

        String provider1 = "provider1";
        String provider2 = "provider2";
        String provider3 = "provider3";

        Patient patient1 = createPatient("patientId1", provider1, PROVIDER_DISTRICT);
        Patient patient2 = createPatient("patientId2", provider2, PROVIDER_DISTRICT);
        Patient patient3 = createPatient("patientId3", provider3, PROVIDER_DISTRICT);

        Integer startIndex = 1;
        Integer rowsPerPage = 2;
        List<Patient> patients = allPatients.getAllUnderActiveTreatmentInDistrictForAGivenPage(PROVIDER_DISTRICT, startIndex, rowsPerPage);
        assertPatientEquals(asList(patient3), patients);
        assertThat(patients.size(), is(1));
    }
}
