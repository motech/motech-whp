package org.motechproject.whp.functional.test.patient;

import org.junit.Test;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.provider.ProviderPage;
import org.motechproject.whp.functional.test.BasePatientTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListAllPatientsForProviderTest extends BasePatientTest {

    @Test
    public void shouldLoginAsProviderAndListAllPatientsForProvider() {
        ProviderPage providerPage = loginAsProvider();
        assertTrue(providerPage.hasPatient(testPatient.getFirstName()));
        assertEquals("Male", providerPage.getGenderText(testPatient.getCaseId()));
        assertEquals("village", providerPage.getVillageText(testPatient.getCaseId()));
    }

    ProviderPage loginAsProvider() {
        return MyPageFactory.initElements(webDriver, LoginPage.class).loginWithProviderUserNamePassword(provider.getProviderId(), provider.getPassword());
    }
}
