package org.motechproject.whp.functional.test.cmfadmin;

import org.junit.Before;
import org.junit.Test;
import org.motechproject.whp.functional.data.TestCmfAdmin;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.LoginPage;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.page.admin.EditCmfAdminPage;
import org.motechproject.whp.functional.page.admin.ItAdminLandingPage;
import org.motechproject.whp.functional.page.admin.ListAllCmfAdminsPage;

import static junit.framework.Assert.assertNotNull;
import static org.motechproject.whp.functional.framework.MyPageFactory.initElements;
import static org.motechproject.whp.functional.page.LoginPage.loginAsItAdmin;

public class CMFAdminTests extends BaseTest {

    @Before
    public void setup() {
        loginAsItAdmin(webDriver);
    }

    @Test
    public void shouldCreateCmfAdmin() throws InterruptedException {
        TestCmfAdmin cmfAdmin = initElements(webDriver, ItAdminLandingPage.class)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage()
                .createCmfAdmin("oldPassword", "Delhi", "oldEmail@a.com", "oldStaffName", "oldDepartment");

        ListAllCmfAdminsPage listAllCmfAdminsPage = Page.getListAllCmfAdminsPage(webDriver);

        assertNotNull(listAllCmfAdminsPage);
        assertNotNull(listAllCmfAdminsPage.getCmfAdminRow(cmfAdmin.getUserId()));

        EditCmfAdminPage editPage = listAllCmfAdminsPage.getEditCmfAdminPage(cmfAdmin.getUserId());
        editPage.assertValues("Delhi", "oldEmail@a.com", "oldStaffName", "oldDepartment");
    }

    @Test
    public void shouldEditCmfAdmin() {
        TestCmfAdmin cmfAdmin = initElements(webDriver, ItAdminLandingPage.class)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage().createCmfAdmin("oldPassword", "Delhi", "oldEmail@a.com", "oldStaffName", "oldDepartment");

        EditCmfAdminPage editCmfAdminPage = Page.getListAllCmfAdminsPage(webDriver).getEditCmfAdminPage(cmfAdmin.getUserId());

        ListAllCmfAdminsPage listAllCmfAdminsPage = editCmfAdminPage.updateCmfAdmin(cmfAdmin.getUserId(), "Patna", "newEmail@a.com", "newStaffName", "newDepartment");
        assertNotNull(listAllCmfAdminsPage);

        EditCmfAdminPage pageAfterEditing = listAllCmfAdminsPage.getEditCmfAdminPage(cmfAdmin.getUserId());
        pageAfterEditing.assertValues("Patna", "newEmail@a.com", "newStaffName", "newDepartment");

    }

    @Test
    public void shouldResetPasswordForCmfAdmin_uponConfirmation() throws InterruptedException {
        TestCmfAdmin testCmfAdmin = initElements(webDriver, ItAdminLandingPage.class)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage().createCmfAdmin();

        ListAllCmfAdminsPage listAllCmfAdminsPage = Page.getListAllCmfAdminsPage(webDriver);

        assertResetPasswordForCmfAdmin(listAllCmfAdminsPage, testCmfAdmin);
    }

    @Test
    public void shouldNotResetPasswordForCmfAdmin_uponCancellation() throws InterruptedException {
        TestCmfAdmin cmfAdmin = initElements(webDriver, ItAdminLandingPage.class)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage().createCmfAdmin();

        ListAllCmfAdminsPage listAllCmfAdminsPage = Page.getListAllCmfAdminsPage(webDriver);

        LoginPage loginPage = listAllCmfAdminsPage.openResetPasswordDialog(cmfAdmin.getUserId())
                .cancelResetPasswordDialog()
                .logout();

        loginPage.loginAsCmfAdminWith(cmfAdmin.getUserId(), cmfAdmin.getPassword()).logout();
    }

    private void assertResetPasswordForCmfAdmin(ListAllCmfAdminsPage listAllCmfAdminsPage, TestCmfAdmin cmfAdmin) {
        LoginPage loginPage = listAllCmfAdminsPage.resetPassword(cmfAdmin.getUserId())
                .logout();

        loginPage.loginAsCmfAdminWith(cmfAdmin.getUserId(), cmfAdmin.getPassword()).logout();
    }
}
