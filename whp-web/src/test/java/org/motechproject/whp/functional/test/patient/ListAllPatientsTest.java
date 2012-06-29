package org.motechproject.whp.functional.test.patient;

import org.junit.Test;
import org.motechproject.whp.functional.page.provider.ListPatientsPage;
import org.motechproject.whp.functional.test.BasePatientTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListAllPatientsTest extends BasePatientTest {

    @Test
    public void shouldListAllPatientsBelongingToTheDefaultDistrictWhenCMFAdminLogsIn() {
        ListPatientsPage providerPage = loginAsCMFAdmin();
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", providerPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", providerPage.getVillageText(testPatient.getCaseId()));
    }
}
