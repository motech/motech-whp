package org.motechproject.whp.patient.repository.allPatients;

import org.junit.Test;
import org.motechproject.whp.patient.domain.Patient;
import org.motechproject.whp.refdata.domain.TreatmentOutcome;

import static junit.framework.Assert.assertTrue;
import static org.motechproject.util.DateUtil.now;
import static org.motechproject.whp.patient.assertUtil.PatientAssert.assertPatientEquals;

public class FetchAllPatientsTestPart extends AllPatientsTestPart {

    @Test
    public void shouldFetchPatientsWithActiveTreatment() {
        Patient withoutActiveTreatment = createPatient("patientId1", "providerId1");
        withoutActiveTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        withoutActiveTreatment.setOnActiveTreatment(false);
        allPatients.update(withoutActiveTreatment);

        Patient withActiveTreatment = createPatient("patientId2", "providerId1");
        assertPatientEquals(new Patient[]{withActiveTreatment}, allPatients.getAllWithActiveTreatment().toArray());
    }

    @Test
    public void shouldNotFetchPatientWithoutActiveTreatment() {
        Patient withoutActiveTreatment = createPatient("patientId", "providerId1");
        withoutActiveTreatment.closeCurrentTreatment(TreatmentOutcome.Cured, now());
        withoutActiveTreatment.setOnActiveTreatment(false);

        allPatients.update(withoutActiveTreatment);
        assertTrue(allPatients.getAllWithActiveTreatment().isEmpty());
    }

    @Test
    public void shouldFetchPatientsBelongingToAllProviders() {
        Patient belongingToFirstProvider = createPatient("patientId1", "providerId1");
        Patient belongingToSecondProvider = createPatient("patientId2", "providerId1");
        assertPatientEquals(
                new Patient[]{belongingToFirstProvider, belongingToSecondProvider},
                allPatients.getAllWithActiveTreatment().toArray());
    }

    @Test
    public void shouldFetchPatientsBelongingToDistrict() {
        String district1 = "Vaishali";
        String district2 = "Begusarai";

        Patient patient1withDistrict1 = createPatient("patientId1", "providerId1", district1);
        Patient patient2withDistrict1 = createPatient("patientId2", "providerId1", district1);

        Patient patient3withDistrict2 = createPatient("patientId3", "providerId1", district2);
        Patient patient4withDistrict2 = createPatient("patientId4", "providerId1", district2);

        assertPatientEquals(
                new Patient[]{patient1withDistrict1, patient2withDistrict1},
                allPatients.getAllWithActiveTreatmentForDistrict(district1).toArray());
        assertPatientEquals(
                new Patient[]{patient3withDistrict2, patient4withDistrict2},
                allPatients.getAllWithActiveTreatmentForDistrict(district2).toArray());
    }
}
