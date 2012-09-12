package org.motechproject.whp.functional.page.admin.listprovider;

import org.junit.Ignore;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.motechproject.whp.functional.framework.WebDriverFactory;
import org.motechproject.whp.functional.page.LoggedInUserPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import java.util.List;

import static org.motechproject.whp.functional.framework.MyPageFactory.initElements;

@Ignore("Ignored until pagination is completed on Provider page")
public class ListProvidersPage extends LoggedInUserPage {

    protected ListProviderFilter listProviderFilter;

    protected ProviderActivation providerActivation;

    protected ProviderResetPassword resetPassword;

    private final ProviderPagination providerPagination;

    private final String providerUnderTest;

    @FindBy(how = How.CLASS_NAME, using = "warning")
    protected WebElement warning;

    @FindBy(how = How.CLASS_NAME, using = "provider-row")
    protected List<WebElement> providers;


    public ListProvidersPage(WebDriver webDriver, String providerId, boolean shouldReset) {
        super(webDriver);
        this.providerUnderTest = providerId;
        if(shouldReset)
            loadFirstPage();
        providerPagination = new ProviderPagination(webDriver, providerId);
        listProviderFilter = new ListProviderFilter(webDriver);
        providerActivation = new ProviderActivation(webDriver, providerPagination);
        resetPassword = new ProviderResetPassword(webDriver, providerPagination);
    }

    private void loadFirstPage() {
        WebElement firstPageLink = safeFindElement(By.linkText("First"));
        if (firstPageLink != null) {
            firstPageLink.click();
        }
        waitForScript(1000);
    }

    public static ListProvidersPage fetch(WebDriver webDriver, String providerId) {
        webDriver.get(WHPUrl.baseFor("provider/list"));
        ListProvidersPage listProvidersPage = new ListProvidersPage(webDriver, providerId, true);
        initElements(webDriver, listProvidersPage);
        return listProvidersPage;
    }

    @Override
    public void postInitialize() {
        super.postInitialize();
        warning = WebDriverFactory.createWebElement(warning);
        initElements(webDriver, listProviderFilter);
        initElements(webDriver, providerActivation);
        initElements(webDriver, resetPassword);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("search");
    }

    public boolean hasProviderRow() {
        return providerPagination.getProviderRow() != null;
    }

    /**
     * @param providerId need not be be provider under test
     */
    public ListProvidersPage searchBy(String district, String providerId, boolean expectingResult) {
        listProviderFilter.searchBy(district, providerId, expectingResult);
        return this;
    }

    public String getWarningText() {
        return warning.getText();
    }

    public boolean isProviderActive() {
        return providerActivation.isProviderActive(providerUnderTest);
    }

    public boolean hasActiveStatus() {
        return providerActivation.hasActiveStatus(providerUnderTest);
    }

    public boolean hasActivateButton() {
        return providerActivation.hasActivateButton(providerUnderTest);
    }

    public ListProvidersPage activateProvider(String password) throws InterruptedException {
        providerActivation.activateProvider(providerUnderTest, password);
        return this;
    }

    public ListProvidersPage validateEmptyPasswordOnActivation() {
        providerActivation.validateEmptyPasswordOnActivation();
        return this;
    }

    public ListProvidersPage validateValidPasswordUponActivation(String password) {
        providerActivation.validateValidPasswordUponActivation(password);
        return this;
    }

    public ListProvidersPage closeProviderActivationModal() throws InterruptedException {
        providerActivation.closeProviderActivationModal();
        return this;
    }

    public ListProvidersPage validatePasswordLengthUponActivation() {
        providerActivation.validatePasswordLengthUponActivation();
        return this;
    }

    public ListProvidersPage validateConfirmPasswordUponActivation(String password) {
        providerActivation.validateConfirmPasswordUponActivation(password);
        return this;
    }

    public ListProvidersPage openActivateProviderModal() throws InterruptedException {
        providerActivation.openActivateProviderModal(providerUnderTest);
        return this;
    }

    public boolean hasResetPasswordLink() {
        return resetPassword.hasResetPasswordLink(providerUnderTest);
    }

    public ListProvidersPage resetPassword() {
        resetPassword.resetPassword(providerUnderTest);
        return this;
    }

    public ListProvidersPage openResetPasswordModal() {
        resetPassword.openResetPasswordModal(providerUnderTest);
        return this;
    }

    public ListProvidersPage cancelResetPasswordDialog() {
        resetPassword.cancelResetPasswordDialog();
        return this;
    }


}
