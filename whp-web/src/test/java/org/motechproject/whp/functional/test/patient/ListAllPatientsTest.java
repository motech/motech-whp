package org.motechproject.whp.functional.test.patient;

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
    public void shouldListAllPatientsForTheDistrictSelectedByCMFAdmin() {
        String districtName = "Vaishali";
        setupTestProvider(districtName);
        setupTestPatientForDistrict(districtName);

        ListAllPatientsPage listPage = loginAsCMFAdmin();
        listPage.searchByDistrict(districtName);
        assertTrue(listPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", listPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", listPage.getVillageText(testPatient.getCaseId()));
    }

    @Test
    public void shouldListAllPatientsForTheDistrictUnderAProviderSelectedByCMFAdmin() {
        String district = "Vaishali";
        setupTestProvider(district);
        setupTestPatientForDistrict(district);

        ListAllPatientsPage listPage = loginAsCMFAdmin();
        listPage.searchByDistrictAndProvider(district, "provider");
        assertTrue(listPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", listPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", listPage.getVillageText(testPatient.getCaseId()));
    }
}
