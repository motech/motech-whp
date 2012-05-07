package org.motechproject.whp.functional.serial;

import org.junit.Ignore;
import org.junit.Test;
import org.motechproject.whp.functional.framework.BaseTest;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.motechproject.whp.functional.page.LoginPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ChangePasswordTest extends BaseTest {

    @Test
    @Ignore
    public void testChangePasswordForAdministrator() {
        LoggedInUserPage adminPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserNamePassword().openChangePasswordModal();
        verifyValidations(adminPage);

        String newPassword = "p@ssw0rd";
        changePassword(adminPage, LoginPage.CORRECT_PASSWORD, newPassword).logout();

        adminPage = MyPageFactory.initElements(webDriver, LoginPage.class).loginWithCorrectAdminUserAnd(newPassword).openChangePasswordModal();
        changePassword(adminPage, newPassword, LoginPage.CORRECT_PASSWORD).logout();
    }

    private void verifyValidations(LoggedInUserPage adminPage) {
        validateMandatoryFields(adminPage);
        validateCurrentPasswordToMatchExistingPassword(adminPage);
        validateNewPasswordForLength(adminPage);
        validateNewPasswordToNotMatchCurrentPassword(adminPage);
        validateConfirmNewPasswordToMatchNewPassword(adminPage);
    }

    private void validateMandatoryFields(LoggedInUserPage adminPage) {
        changePasswordAndValidate(adminPage, "", "", "", Arrays.asList("'Current Password' cannot be empty", "'New Password' cannot be empty", "'Confirm New Password' cannot be empty"));
    }

    private void validateCurrentPasswordToMatchExistingPassword(LoggedInUserPage adminPage) {
        changePasswordAndValidate(adminPage, "incorrectoldpassword", "newpassword", "newpassword", Arrays.asList("'Current Password' you entered is incorrect"));
    }

    private void validateNewPasswordForLength(LoggedInUserPage adminPage) {
        changePasswordAndValidate(adminPage, "password", "3ch", "3ch", Arrays.asList("'New Password' should be at least 4 characters long"));
    }

    private void validateNewPasswordToNotMatchCurrentPassword(LoggedInUserPage adminPage) {
        changePasswordAndValidate(adminPage, "password", "password", "password", Arrays.asList("'New Password' should not be the same as the 'Current Password'"));
    }

    private void validateConfirmNewPasswordToMatchNewPassword(LoggedInUserPage adminPage) {
        changePasswordAndValidate(adminPage, "password", "newpassword", "notnewpassword", Arrays.asList("'Confirm New Password' should match 'New Password'"));
    }

    private LoggedInUserPage changePassword(LoggedInUserPage adminPage, String oldPassword, String newPassword) {
        changePasswordAndValidate(adminPage, oldPassword, newPassword, newPassword, new ArrayList<String>());
        return adminPage;
    }

    private void changePasswordAndValidate(LoggedInUserPage adminPage, String currentPasswordText, String newPasswordText, String confirmNewPasswordText, List<String> errorMessages) {
        adminPage.changePassword(currentPasswordText, newPasswordText, confirmNewPasswordText);
        List<String> changePasswordErrorMessages = adminPage.getChangePasswordErrorMessages();
        for (String errorMessage : errorMessages) {
            assertTrue(changePasswordErrorMessages.contains(errorMessage));
        }
    }
}
