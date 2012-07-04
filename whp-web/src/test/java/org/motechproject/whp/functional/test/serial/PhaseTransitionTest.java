package org.motechproject.whp.functional.test.serial;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestPatient;
import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.page.admin.AdminPage;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.page.admin.TreatmentCardPage;
import org.motechproject.whp.functional.steps.provideradherence.SubmitAdherenceStep;
import org.motechproject.whp.functional.test.treatmentupdate.TreatmentUpdateTest;
import org.motechproject.whp.refdata.domain.PhaseName;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class PhaseTransitionTest extends TreatmentUpdateTest {

    SubmitAdherenceStep submitAdherenceStep;

    @Before
    public void setup() {
        submitAdherenceStep = new SubmitAdherenceStep(webDriver);
    }

    @Test
    public void shouldTransitionPatientFromIPToEIP_When24thDoseIsCapturedAndAdminHasMadeDecisionBeforeEndOfPhase() {
        adjustDateTime(1, 7, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

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
                .withDosesTaken(3)
                .execute();

        adjustDateTime(12, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        transitionPatient(testPatient, testProvider, PhaseName.EIP);

        assertPatientIsOnIP(testPatient);

        adjustDateTime(19, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        assertPatientIsOnEIP(testPatient);
    }

    @Test
    public void shouldTransitionPatientFromIPToCP_When27thDoseIsCapturedAndAdminHasMadeDecisionAfterEndOfPhase() {
        setupProvider();

        setupPatientForProvider();

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
                .withDosesTaken(3)
                .execute();

        adjustDateTime(12, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(19, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        assertPatientIsOnNoPhase(testPatient);

        adjustDateTime(26, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        transitionPatient(testPatient, testProvider, PhaseName.CP);

        assertPatientIsOnCP(testPatient);
    }

    @Test
    public void shouldTransitionPatientFromEIPToCP_When15thDoseIsCapturedAndAdminHasMadeDecisionAfterEndOfPhase() {
        setupProvider();
        setupPatientForProvider();

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
                .withDosesTaken(3)
                .execute();

        adjustDateTime(12, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(19, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        assertPatientIsOnNoPhase(testPatient);

        transitionPatient(testPatient, testProvider, PhaseName.EIP);

        assertPatientIsOnEIP(testPatient);

        adjustDateTime(26, 8, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(2, 9, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(9, 9, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        adjustDateTime(16, 9, 2012);

        submitAdherenceStep
                .withProvider(testProvider)
                .withPatient(testPatient)
                .withDosesTaken(3)
                .execute();

        assertPatientIsOnNoPhase(testPatient);

        transitionPatient(testPatient, testProvider, PhaseName.CP);

        assertPatientIsOnCP(testPatient);
    }

    private void transitionPatient(TestPatient testPatient, TestProvider testProvider, PhaseName phaseName) {
        AdminPage adminPage = loginAsAdmin();
        ListAllPatientsPage listAllPatientsPage = adminPage.navigateToShowAllPatients();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        TreatmentCardPage treatmentCardPage = listAllPatientsPage.clickOnPatientWithTherapyStarted(testPatient.getCaseId());
        treatmentCardPage.transitionPatient(phaseName);
        treatmentCardPage.logout();
    }

    private void assertPatientIsOnIP(TestPatient testPatient) {
        AdminPage adminPage = loginAsAdmin();
        ListAllPatientsPage listAllPatientsPage = adminPage.navigateToShowAllPatients();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        TreatmentCardPage treatmentCardPage = listAllPatientsPage.clickOnPatientWithTherapyStarted(testPatient.getCaseId());
        assertTrue(treatmentCardPage.currentPhase(PhaseName.IP));
        treatmentCardPage.logout();
    }

    private void assertPatientIsOnEIP(TestPatient testPatient) {
        AdminPage adminPage = loginAsAdmin();
        ListAllPatientsPage listAllPatientsPage = adminPage.navigateToShowAllPatients();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        TreatmentCardPage treatmentCardPage = listAllPatientsPage.clickOnPatientWithTherapyStarted(testPatient.getCaseId());
        assertTrue(treatmentCardPage.currentPhase(PhaseName.EIP));
        treatmentCardPage.logout();
    }

    private void assertPatientIsOnCP(TestPatient testPatient) {
        AdminPage adminPage = loginAsAdmin();
        ListAllPatientsPage listAllPatientsPage = adminPage.navigateToShowAllPatients();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        TreatmentCardPage treatmentCardPage = listAllPatientsPage.clickOnPatientWithTherapyStarted(testPatient.getCaseId());
        assertTrue(treatmentCardPage.currentPhase(PhaseName.CP));
        treatmentCardPage.logout();
    }

    private void assertPatientIsOnNoPhase(TestPatient testPatient) {
        AdminPage adminPage = loginAsAdmin();
        ListAllPatientsPage listAllPatientsPage = adminPage.navigateToShowAllPatients();
        listAllPatientsPage = listAllPatientsPage.searchByDistrictAndProvider(testPatient.getDistrict(), testProvider.getProviderId());
        TreatmentCardPage treatmentCardPage = listAllPatientsPage.clickOnPatientWithTherapyStarted(testPatient.getCaseId());
        assertFalse(treatmentCardPage.currentPhase(PhaseName.IP));
        assertFalse(treatmentCardPage.currentPhase(PhaseName.EIP));
        assertFalse(treatmentCardPage.currentPhase(PhaseName.CP));
        treatmentCardPage.logout();
    }

}