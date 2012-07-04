package org.motechproject.whp.functional.test.patient;

import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.whp.functional.page.admin.ListAllPatientsPage;
import org.motechproject.whp.functional.test.BasePatientTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListAllPatientsTest extends BasePatientTest {

    @Test
    public void shouldListAllPatientsBelongingToTheDefaultDistrictWhenCMFAdminLogsIn() {
        setupProvider();
        setupPatientForProvider();

        ListAllPatientsPage listPage = loginAsCMFAdmin();
        assertTrue(listPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", listPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", listPage.getVillageText(testPatient.getCaseId()));
    }

    @Test
    @Ignore("to be fixed. search button is not clicked for some reason.")
    public void shouldListAllPatientsForTheDistrictSelectedByCMFAdmin() {
        String districtName = "Vaishali";
        setupTestProvider(districtName);
        setupTestPatientForDistrict(districtName);

        ListAllPatientsPage listPage = loginAsCMFAdmin();
        listPage.searchByDistrict(districtName);
        listPage.getSearchButton().click();
        assertTrue(listPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", listPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", listPage.getVillageText(testPatient.getCaseId()));
    }

    @Test
    @Ignore("to be fixed. search button is not clicked for some reason.")
    public void shouldListAllPatientsForTheDistrictUnderAProviderSelectedByCMFAdmin() {
        String district = "Vaishali";
        setupTestProvider(district);
        setupTestPatientForDistrict(district);

        ListAllPatientsPage listPage = loginAsCMFAdmin();

        listPage.searchByDistrictAndProvider(district, provider.getProviderId());
        assertTrue(listPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", listPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", listPage.getVillageText(testPatient.getCaseId()));
    }
}
