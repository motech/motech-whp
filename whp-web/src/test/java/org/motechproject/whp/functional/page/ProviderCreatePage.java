package org.motechproject.whp.functional.page;

import org.motechproject.whp.functional.data.TestProvider;
import org.motechproject.whp.functional.framework.MyPageFactory;
import org.motechproject.whp.functional.framework.WHPUrl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

public class ProviderCreatePage extends Page {

    @FindBy(how = How.ID, using = "provider_id")
    WebElement providerId;
    @FindBy(how = How.ID, using = "password")
    WebElement password;
    @FindBy(how = How.ID, using = "primary_mobile")
    WebElement primaryMobile;
    @FindBy(how = How.ID, using = "secondary_mobile")
    WebElement secondaryMobile;
    @FindBy(how = How.ID, using = "tertiary_mobile")
    WebElement tertiaryMobile;
    @FindBy(how = How.ID, using = "district")
    WebElement district;
    @FindBy(how = How.ID, using = "post-button")
    WebElement submit;

    public ProviderCreatePage(WebDriver webDriver) {
        super(webDriver);
    }

    @Override
    protected void waitForPageToLoad() {
        waitForElementWithIdToLoad("provider_id");
    }

    public static ProviderCreatePage fetch(WebDriver webDriver) {
        webDriver.get(WHPUrl.baseFor("emulator/provider.jsp"));
        return MyPageFactory.initElements(webDriver, ProviderCreatePage.class);
    }

    public void createProviderWithLogin(TestProvider provider) {
        setProviderId(provider.getProviderId());
        setPassword(provider.getPassword());
        setPrimaryMobile(provider.getPrimaryMobile());
        setSecondaryMobile(provider.getSecondaryMobile());
        setTertiaryMobile(provider.getTertiaryMobile());
        setDistrict(provider.getDistrict());
        submit.click();
    }

    private void setProviderId(String providerId) {
        this.providerId.clear();
        this.providerId.sendKeys(providerId);
    }

    private void setPassword(String password) {
        this.password.clear();
        this.password.sendKeys(password);
    }

    private void setDistrict(String district) {
        this.district.clear();
        this.district.sendKeys(district);
    }

    private void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile.clear();
        this.primaryMobile.sendKeys(primaryMobile);
    }

    private void setSecondaryMobile(String secondaryMobile) {
        this.secondaryMobile.clear();
        this.secondaryMobile.sendKeys(secondaryMobile);
    }

    private void setTertiaryMobile(String tertiaryMobile) {
        this.tertiaryMobile.clear();
        this.tertiaryMobile.sendKeys(tertiaryMobile);
    }
}
