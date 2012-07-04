package org.motechproject.whp.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.util.DateUtil;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class SearchByProviderIdTestPart extends AllPatientsTestPart {

    @Test
    public void shouldFetchPatientsByCurrentProviderId() {
        Patient requiredPatient = createPatient("patientId1", "providerId1");
        createPatient("patientId2", "providerId2");

        assertPatientEquals(new Patient[]{requiredPatient}, allPatients.getAllWithActiveTreatmentFor("providerId1").toArray());
    }

    @Test
    public void shouldFetchPatientsUnderActiveTreatmentByProvider() {
        String providerId = "providerId1";
        Patient requiredPatient = createPatient("patientId1", providerId);

        Patient patientWithTreatmentClosed = createPatient("patientId2", providerId);
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
        Patient patient1 = createPatient("patientId1", "providerId1");
        Patient patient2 = createPatient("patientId2", "providerId1");

        assertPatientEquals(new Patient[]{patient1, patient2}, allPatients.getAllWithActiveTreatmentFor("providerId1").toArray());
    }

    @Test
    public void shouldFetchPatientsForAProviderWithActiveTreatment() {
        Patient withoutActiveTreatment1 = createPatient("patientId1", "providerId1");
        withoutActiveTreatment1.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        allPatients.update(withoutActiveTreatment1);

        Patient withActiveTreatment1 = createPatient("patientId2", "providerId1");

        Patient withoutActiveTreatment2 = createPatient("patientId3", "providerId2");
        withoutActiveTreatment2.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        allPatients.update(withoutActiveTreatment2);

        Patient withActiveTreatment2 = createPatient("patientId4", "providerId2");

        assertPatientEquals(new Patient[]{withActiveTreatment1}, allPatients.getAllWithActiveTreatmentFor("providerId1").toArray());
        assertPatientEquals(new Patient[]{withActiveTreatment2}, allPatients.getAllWithActiveTreatmentFor("providerId2").toArray());
    }

    @Test
    public void shouldFetchPatientsForProvider_sortByFirstName() {
        String providerId = "providerId1";

        Patient withGreatestFirstNameButCreatedFirst = createPatientOnActiveTreatment("patientId1", "c", providerId);
        Patient createdSecond = createPatientOnActiveTreatment("patientId2", "b", providerId);
        Patient withSmallestFirstNameButCreatedLast = createPatientOnActiveTreatment("patientId3", "a", providerId);

        assertPatientEquals(
                asList(withSmallestFirstNameButCreatedLast, createdSecond, withGreatestFirstNameButCreatedFirst),
                allPatients.getAllWithActiveTreatmentFor(providerId)
        );
    }

    @Test
    public void shouldFetchByProviderIds() {
        String provider1 = "provider1";
        String provider2 = "provider2";
        String provider3 = "provider3";

        Patient patient1 = createPatient("patientId1", provider1);
        Patient patient2 = createPatient("patientId2", provider2);
        Patient patient3 = createPatient("patientId3", provider3);

        List<Patient> patients = allPatients.getAllUnderActiveTreatmentWithCurrentProviders(asList(provider1, provider3));
        assertPatientEquals(asList(patient1, patient3), patients);
    }
}
