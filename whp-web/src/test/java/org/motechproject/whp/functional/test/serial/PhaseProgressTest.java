package org.motechproject.whp.functional.test.serial;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.steps.LoginAsCmfAdminStep;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;
import org.motechproject.whp.refdata.domain.Phase;

import static org.junit.Assert.assertEquals;

public class PhaseProgressTest extends TreatmentUpdateTest {

    SubmitAdherenceStep submitAdherenceStep;
    LoginAsCmfAdminStep loginAsCmfAdminStep;

    @Before
    public void setup() {
        submitAdherenceStep = new SubmitAdherenceStep(webDriver);
        loginAsCmfAdminStep = new LoginAsCmfAdminStep(webDriver);
    }

    @Test
    public void shouldReflectIPProgressOnPatientListPage() {
        adjustDateTime(1, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(8, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(15, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(22, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(29, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(5, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(2)
                .execute();

        adjustDateTime(12, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(2)
                .execute();

        adjustDateTime(19, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(26, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        assertProgressOnPhase(Phase.IP, "25/24 (104.17%)");
        assertCumulativeMissedDoses(2);
    }

    private void assertCumulativeMissedDoses(int numberOfMissedDoses) {
        ListAllPatientsPage listAllPatientsPage = loginAsCmfAdminStep.execute();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        String cumulativeMissedDoses = listAllPatientsPage.getCumulativeMissedDoses(testPatient.getCaseId());
        assertEquals(String.valueOf(numberOfMissedDoses), cumulativeMissedDoses);
    }

    private void assertProgressOnPhase(Phase phase, String completionPercentage) {
        ListAllPatientsPage listAllPatientsPage = loginAsCmfAdminStep.execute();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        String phaseProgress = listAllPatientsPage.getPhaseProgress(testPatient.getCaseId(), phase);
        listAllPatientsPage.logout();
        assertEquals(completionPercentage, phaseProgress);
    }

}