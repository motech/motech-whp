package org.motechproject.whp.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import java.util.ArrayList;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class SearchByProviderIdTestPart extends AllPatientsTestPart {

    @Test
    public void shouldFetchPatientsByCurrentProviderId() {
        Patient requiredPatient = createPatient("patientId1", "providerId1");
        createPatient("patientId2", "providerId2");

        assertPatientEquals(new Patient[]{requiredPatient}, allPatients.findByCurrentProviderId("providerId1").toArray());
    }

    @Test
    public void fetchByCurrentProviderIdShouldReturnEmptyListIfKeywordIsNull() {
        assertEquals(new ArrayList<Patient>(), allPatients.findByCurrentProviderId(null));
    }

    @Test
    public void shouldFetchAllPatientsForAProvider() {
        Patient patient1 = createPatient("patientId1", "providerId1");
        Patient patient2 = createPatient("patientId2", "providerId1");

        assertPatientEquals(new Patient[]{patient1, patient2}, allPatients.findByCurrentProviderId("providerId1").toArray());
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
}
