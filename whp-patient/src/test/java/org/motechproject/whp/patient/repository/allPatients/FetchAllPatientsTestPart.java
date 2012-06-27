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
                allPatients.getAllWithActiveTreatment().toArray()
        );
    }
}
