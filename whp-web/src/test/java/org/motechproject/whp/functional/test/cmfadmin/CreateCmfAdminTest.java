package org.motechproject.whp.functional.test.cmfadmin;

import org.junit.Test;
import org.motechproject.whp.functional.data.TestCmfAdmin;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.page.Page;
import org.motechproject.whp.functional.page.admin.EditCmfAdminPage;
import org.motechproject.whp.functional.page.admin.ListAllCmfAdminsPage;

import static junit.framework.Assert.assertNotNull;
import static org.motechproject.whp.functional.page.LoginPage.loginAsItAdmin;

public class CreateCmfAdminTest extends BaseTest {

    @Test
    public void shouldCreateCmfAdmin() throws InterruptedException {
        TestCmfAdmin cmfAdmin = loginAsItAdmin(webDriver)
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

        TestCmfAdmin cmfAdmin = loginAsItAdmin(webDriver)
                .navigateToSearchCmfAdmins()
                .navigateToCreateCmfAdminPage().createCmfAdmin("oldPassword", "Delhi", "oldEmail@a.com", "oldStaffName", "oldDepartment");

        EditCmfAdminPage editCmfAdminPage = Page.getListAllCmfAdminsPage(webDriver).getEditCmfAdminPage(cmfAdmin.getUserId());

        ListAllCmfAdminsPage listAllCmfAdminsPage = editCmfAdminPage.updateCmfAdmin(cmfAdmin.getUserId(), "Patna", "newEmail@a.com", "newStaffName", "newDepartment");
        assertNotNull(listAllCmfAdminsPage);

        EditCmfAdminPage pageAfterEditing = listAllCmfAdminsPage.getEditCmfAdminPage(cmfAdmin.getUserId());
        pageAfterEditing.assertValues("Patna", "newEmail@a.com", "newStaffName", "newDepartment");

    }

}
